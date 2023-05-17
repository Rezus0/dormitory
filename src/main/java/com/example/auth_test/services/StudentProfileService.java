package com.example.auth_test.services;

import com.example.auth_test.model.statement.Block;
import com.example.auth_test.model.user.StudentProfile;
import com.example.auth_test.model.user.User;
import com.example.auth_test.repos.BlockRepository;
import com.example.auth_test.repos.StudentProfileRepository;
import com.example.auth_test.requests.ProfileResponse;
import com.example.auth_test.requests.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentProfileService {
    private final StudentProfileRepository studentProfileRepository;
    private final BlockService blockService;
    public ProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        String blockNumber = request.getBlockNumber();
        StudentProfile profile;
        if (studentProfileRepository.findByUser(user).isEmpty()) {
            profile = StudentProfile.builder()
                    .baseOfTraining(request.getBaseOfTraining())
                    .course(request.getCourse())
                    .groupName(request.getGroupName())
                    .trainingForm(request.getTrainingForm())
                    .institute(request.getInstitute())
                    .user(user)
                    .build();
        } else {
            profile = studentProfileRepository.findByUser(user).orElseThrow();
            profile.setCourse(request.getCourse());
            profile.setInstitute(request.getInstitute());
            profile.setGroupName(request.getGroupName());
            profile.setTrainingForm(request.getTrainingForm());
            profile.setBaseOfTraining(request.getBaseOfTraining());
        }
        if (!blockService.blockIsPresent(blockNumber)) {
            Block block = new Block(blockNumber);
            return getProfileResponse(profile, block);
        } else {
            Block block = blockService.getBlock(blockNumber);
            return getProfileResponse(profile, block);
        }
    }

    private ProfileResponse getProfileResponse(StudentProfile profile, Block block) {
        profile.setBlock(block);
        List<StudentProfile> students = block.getStudents();
        students.add(profile);
        block.setStudents(students);
        blockService.saveBlock(block);
        studentProfileRepository.save(profile);
        return ProfileResponse.builder()
                .baseOfTraining(profile.getBaseOfTraining())
                .course(profile.getCourse())
                .trainingForm(profile.getTrainingForm())
                .groupName(profile.getGroupName())
                .institute(profile.getInstitute())
                .blockNumber(profile.getBlock().getNumber())
                .build();
    }
}
