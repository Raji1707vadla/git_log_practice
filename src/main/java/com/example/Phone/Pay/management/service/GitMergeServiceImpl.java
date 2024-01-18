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
                String fromBranch = gitCredentialsDto.getFromBranch();
                String toBranch = gitCredentialsDto.getToBranch();
                git.fetch().setRemote("origin").setRefSpecs(new RefSpec("refs/heads/"+fromBranch + ":" + "refs/heads/"+toBranch)).call();
                Ref sourceRef = repository.findRef("refs/heads/" + fromBranch);
                if (sourceRef == null) {
                    return "Source branch does not exist: " + fromBranch;
                }

                Ref targetRef = repository.findRef("refs/heads/" + toBranch);
                if (targetRef == null) {
                    return "Target branch does not exist: " + toBranch;
                }
                MergeResult mergeResult = git.merge().include(repository.resolve(fromBranch)).setCommit(true).setFastForward(MergeCommand.FastForwardMode.NO_FF).call();
                if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)) {
                    return "Merge conflicts detected. Please resolve conflicts before merging.";
                } else if (mergeResult.getMergeStatus().isSuccessful()) {
                    git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitCredentialsDto.getUserName(), gitCredentialsDto.getPassword())).setRemote("origin").setRefSpecs(new RefSpec(toBranch + ":" + toBranch)).call();
                    return "Successfully Merged and Pushed Code";
                } else {
                    return "Merge Failed";
                }
            }catch (Exception e){
                return "You Don't have access";
            }
        } catch (Exception e) {
            return "Please Commit Your Changes Before pull.";
        }
    }

    private boolean checkReadAccess(GitCredentialsDto gitCredentialsDto) {
        try {
            try (Git git = Git.open(new File(gitCredentialsDto.getRepository()))) {
                List<Ref> branches = git.branchList().call();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }



}
