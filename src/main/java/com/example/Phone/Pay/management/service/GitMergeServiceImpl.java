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
            if (Boolean.FALSE.equals(checkReadAccess(gitCredentialsDto)) ){
                return "You don't have read access to the repository.";
            }
            try (Git git = Git.open(new File(gitCredentialsDto.getRepository()))) {
                Repository repository = git.getRepository();
                System.out.println("Source Branch: " + gitCredentialsDto.getSourceBranch());
                System.out.println("Target Branch: " + gitCredentialsDto.getTargetBranch());
                Ref sourceRef = repository.findRef("refs/heads/" + gitCredentialsDto.getSourceBranch());
                if (sourceRef == null) {
                    System.out.println("Source branch does not exist: " + gitCredentialsDto.getSourceBranch());
                    return "Source branch does not exist: " + gitCredentialsDto.getSourceBranch();
                }
                Ref targetRef = repository.findRef("refs/heads/" + gitCredentialsDto.getTargetBranch());
                if (targetRef == null) {
                    System.out.println("Target branch does not exist: " + gitCredentialsDto.getTargetBranch());
                    return "Target branch does not exist: " + gitCredentialsDto.getTargetBranch();
                }
                MergeResult mergeResult = git.merge().include(repository.resolve(gitCredentialsDto.getSourceBranch())).setCommit(true).setFastForward(MergeCommand.FastForwardMode.NO_FF).call();
                System.out.println("Merge Status: " + mergeResult.getMergeStatus());
                System.out.println("Conflicts: " + mergeResult.getConflicts());

                if (mergeResult.getMergeStatus().isSuccessful()) {
                    System.out.println("Merge successful");
                    git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitCredentialsDto.getUserName(), gitCredentialsDto.getPassword())).setRemote("origin").setRefSpecs(new RefSpec(gitCredentialsDto.getTargetBranch() + ":" + gitCredentialsDto.getTargetBranch())).call();
                    System.out.println("Push successful");
                    System.out.println("Successfully Merged and Pushed");
                    return "Successfully Merged and Pushed";
                } else if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
                    System.out.println("Merge conflicts detected. Please resolve conflicts before merging.");
                    return "Merge conflicts detected. Please resolve conflicts before merging.";
                } else {
                    System.out.println("Merge failed");
                    return "Merge Failed";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, something went wrong";
        }
    }

/*    public String mergeBranches(GitCredentialsDto gitCredentialsDto) {
        try {
            try (Git git = Git.open(new File(gitCredentialsDto.getRepository()))) {
                Repository repository = git.getRepository();

                System.out.println("Source Branch: " + gitCredentialsDto.getSourceBranch());
                System.out.println("Target Branch: " + gitCredentialsDto.getTargetBranch());

                Ref sourceRef = repository.findRef("refs/heads/" + gitCredentialsDto.getSourceBranch());
                if (sourceRef == null) {
                    System.out.println("Source branch does not exist: " + gitCredentialsDto.getSourceBranch());
                    return "Source branch does not exist: " + gitCredentialsDto.getSourceBranch();
                }

                Ref targetRef = repository.findRef("refs/heads/" + gitCredentialsDto.getTargetBranch());
                if (targetRef == null) {
                    System.out.println("Target branch does not exist: " + gitCredentialsDto.getTargetBranch());
                    return "Target branch does not exist: " + gitCredentialsDto.getTargetBranch();
                }

                MergeResult mergeResult = git.merge()
                        .include(repository.resolve(gitCredentialsDto.getSourceBranch()))
                        .setCommit(true)
                        .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                        .call();

                System.out.println("Merge Status: " + mergeResult.getMergeStatus());
                System.out.println("Conflicts: " + mergeResult.getConflicts());

                if (mergeResult.getMergeStatus().isSuccessful()) {
                    System.out.println("Merge successful");

                    // Push changes to the target branch
                    git.push()
                            .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitCredentialsDto.getUserName(), gitCredentialsDto.getPassword()))
                            .setRemote("origin")
                            .setRefSpecs(new RefSpec(gitCredentialsDto.getTargetBranch() + ":" + gitCredentialsDto.getTargetBranch()))
                            .call();

                    System.out.println("Push successful");
                    return "Successfully Merged and Pushed";
                } else {
                    System.out.println("Merge failed");
                    return "Merge Failed";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Sorry, something went wrong";
    }*/

/*    public String mergeBranches(GitCredentialsDto gitCredentialsDto) {
        try {
            if (!checkReadAccess(gitCredentialsDto)) {
                return "You don't have read access to the repository.";
            }

            try (Git git = Git.open(new File(gitCredentialsDto.getRepository()))) {
                Repository repository = git.getRepository();

                System.out.println("Source Branch: " + gitCredentialsDto.getSourceBranch());
                System.out.println("Target Branch: " + gitCredentialsDto.getTargetBranch());

                Ref sourceRef = repository.findRef("refs/heads/" + gitCredentialsDto.getSourceBranch());
                if (sourceRef == null) {
                    System.out.println("Source branch does not exist: " + gitCredentialsDto.getSourceBranch());
                    return "Source branch does not exist: " + gitCredentialsDto.getSourceBranch();
                }

                Ref targetRef = repository.findRef("refs/heads/" + gitCredentialsDto.getTargetBranch());
                if (targetRef == null) {
                    System.out.println("Target branch does not exist: " + gitCredentialsDto.getTargetBranch());
                    return "Target branch does not exist: " + gitCredentialsDto.getTargetBranch();
                }

                MergeResult mergeResult = git.merge()
                        .include(repository.resolve(gitCredentialsDto.getSourceBranch()))
                        .setCommit(true)
                        .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                        .call();

                System.out.println("Merge Status: " + mergeResult.getMergeStatus());
                System.out.println("Conflicts: " + mergeResult.getConflicts());

                if (mergeResult.getMergeStatus().isSuccessful()) {
                    System.out.println("Merge successful");
                    git.push()
                            .setCredentialsProvider(new UsernamePasswordCredentialsProvider("your_username", "your_password"))
                            .setRemote("origin")
                            .setRefSpecs(new RefSpec(gitCredentialsDto.getTargetBranch() + ":" + gitCredentialsDto.getTargetBranch()))
                            .call();

                    System.out.println("Push successful");
                    return "Successfully Merged and Pushed";
                } else {
                    System.out.println("Merge failed");
                    return "Merge Failed";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, something went wrong";
        }
    }*/

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
