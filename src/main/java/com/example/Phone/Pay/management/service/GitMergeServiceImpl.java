package com.example.Phone.Pay.management.service;

import com.example.Phone.Pay.management.dto.GitCredentialsDto;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 18/01/24
 * @Time ➤➤➤ 10:31 am
 * @Project ➤➤➤ Phone-Pay-management
 */
@Service
public class GitMergeServiceImpl implements GitMergeService{

    @Override
    public String mergeBranches(GitCredentialsDto gitCredentialsDto) {
        try {
            if (Boolean.FALSE.equals(checkReadAccess(gitCredentialsDto))) {
                return "You don't have read access to the repository.";
            }

            try (Git git = Git.open(new File(gitCredentialsDto.getRepository()))) {
                Repository repository = git.getRepository();

                // Fetch updates from the remote branch
                git.fetch().setRemote("origin").setRefSpecs(new RefSpec("refs/heads/"+gitCredentialsDto.getTargetBranch() + ":" + "refs/heads/"+gitCredentialsDto.getTargetBranch()))
                        .call();

                Ref sourceRef = repository.findRef("refs/heads/" + gitCredentialsDto.getSourceBranch());
                if (sourceRef == null) {
                    return "Source branch does not exist: " + gitCredentialsDto.getSourceBranch();
                }

                Ref targetRef = repository.findRef("refs/heads/" + gitCredentialsDto.getTargetBranch());
                if (targetRef == null) {
                    return "Target branch does not exist: " + gitCredentialsDto.getTargetBranch();
                }

                // Merge the changes from the source branch
                MergeResult mergeResult = git.merge()
                        .include(repository.resolve(gitCredentialsDto.getSourceBranch()))
                        .setCommit(true)
                        .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                        .call();

                if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
                    return "Merge conflicts detected. Please resolve conflicts before merging.";
                } else if (mergeResult.getMergeStatus().isSuccessful()) {
                    // If merge is successful, push the changes
                    git.push()
                            .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitCredentialsDto.getUserName(), gitCredentialsDto.getPassword()))
                            .setRemote("origin")
                            .setRefSpecs(new RefSpec(gitCredentialsDto.getTargetBranch() + ":" + gitCredentialsDto.getTargetBranch()))
                            .call();
                    return "Successfully Merged and Pushed";
                } else {
                    return "Not Mergble";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, something went wrong";
        }
    }
    private boolean checkReadAccess(GitCredentialsDto gitCredentialsDto) {
        try {
            try (Git git = Git.open(new File(gitCredentialsDto.getRepository()))) {
                List<Ref> branches = git.branchList().call();
                System.out.println("Read access successful");
                return true;
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Authentication is required")) {
                System.out.println("Authentication error: You don't have read access.");
            } else {
                System.out.println("An error occurred: " + e.getMessage());
            }
            return false;
        }
    }



}
