package com.example.Phone.Pay.management.service;

import com.example.Phone.Pay.management.dto.GitCredentialsDto;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.springframework.stereotype.Service;

import java.io.File;

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

                git.checkout().setName(gitCredentialsDto.getTargetBranch()).call();

                MergeResult mergeResult = git.merge()
                        .include(repository.resolve(gitCredentialsDto.getSourceBranch()))
                        .setCommit(true)
                        .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                        .call();

                System.out.println("Merge Status: " + mergeResult.getMergeStatus());
                System.out.println("Conflicts: " + mergeResult.getConflicts());

                if (mergeResult.getMergeStatus().isSuccessful()) {
                    System.out.println("Merge successful");
                    return "Successfully Merged";
                } else {
                    System.out.println("Merge failed");
                    return "Merge Failed";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Sorry Something went wrong";
    }

 /*   public  String mergeBranches(GitCredentialsDto gitCredentialsDto) {
        try {
            try (Git git = Git.open(new File(gitCredentialsDto.getRepository()))) {
                Repository repository = git.getRepository();
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
                git.checkout().setName(gitCredentialsDto.getTargetBranch()).call();
                MergeResult mergeResult = git.merge()
                        .include(repository.resolve(gitCredentialsDto.getSourceBranch()))
                        .setCommit(true)
                        .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                        .call();
                if (mergeResult.getMergeStatus().isSuccessful()) {
                    System.out.println("Merge successful");
                    return "Successfully Merged";
                } else {
                    System.out.println("Merge failed");
                    return "Merge Failed";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Sorry Something went wrong";
    }*/
}
