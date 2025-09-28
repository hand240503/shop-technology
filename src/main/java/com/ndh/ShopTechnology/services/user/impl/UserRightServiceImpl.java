package com.ndh.ShopTechnology.services.user.impl;

import com.ndh.ShopTechnology.constant.SystemConstant;
import com.ndh.ShopTechnology.def.DefAPIAuth;
import com.ndh.ShopTechnology.entities.user.UserAuthRightEntity;
import com.ndh.ShopTechnology.entities.user.UserEntity;
import com.ndh.ShopTechnology.repository.UserRepository;
import com.ndh.ShopTechnology.repository.UserRightRepository;
import com.ndh.ShopTechnology.services.user.UserAuthRightService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRightServiceImpl implements UserAuthRightService {

    private final UserRightRepository userRightRepository;
    private final UserRepository userRepository;


    public UserRightServiceImpl(UserRightRepository userRightRepository, UserRepository userRepository) {
        this.userRightRepository = userRightRepository;
        this.userRepository = userRepository;
    }

    //    @Override
//    public UserAuthRightEntity manageUserRights(UserEntity userEntity, Set<Integer> rightsToAssign, Long managerId, String mode) {
//        if (userEntity.getId() != null && userEntity.getId().equals(managerId) && mode.equals("mod")) {
//            return null;
//        }
//
//        Optional<UserEntity> managerEntityOpt = userRepository.findById(managerId);
//        if (managerEntityOpt.isEmpty()) {
//            return null;
//        }
//
//        UserEntity managerEntity = managerEntityOpt.get();
//
//        UserAuthRightEntity managerAuthRights = userRightRepository.findByUserIdAndType(managerId, DefAPIAuth.AUTH_TYPE_MODIFIABLE);
//        if (managerAuthRights == null) {
//            return null;
//        }
//
//        Set<Integer> managerRightsSet = extractRightsSet(managerAuthRights.getAuthRights());
//
//        Set<Integer> rightsToAssignSet = new HashSet<>();
//        for (Integer right : rightsToAssign) {
//            if (managerRightsSet.contains(right)) {
//                rightsToAssignSet.add(right);
//            }
//        }
//
//        UserAuthRightEntity userAuthRights;
//
//        // Tùy theo mode, thực hiện thao tác tương ứng
//        if (mode.equals("new")) {
//            userAuthRights = new UserAuthRightEntity();
//            userAuthRights.setAuthRights(rightsToAssignSet.toString());
//            userAuthRights.setStatus(SystemConstant.ACTIVE_STATUS);
//            userAuthRights.setType(0);
//        } else if (mode.equals("mod")) {
//            userAuthRights = userRightRepository.findByUserIdAndType(userEntity.getId(), DefAPIAuth.AUTH_TYPE_MODIFIABLE);
//            if (userAuthRights == null) {
//                return null;
//            }
//            userAuthRights.setAuthRights(rightsToAssignSet.toString());
//        } else {
//            return null; // Trả về null nếu mode không hợp lệ
//        }
//
//        return userRightRepository.save(userAuthRights);
//    }
//
    private Set<Integer> extractRightsSet(String rights) {
        if (rights == null || rights.isBlank()) return Set.of();

        return Arrays.stream(
                        rights.replace("[", "").replace("]", "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }


    @Override
    public UserAuthRightEntity manageUserRights(int mode, UserEntity user, Set<Integer> rights, Long manId) {
        if (user == null || rights == null) return null;

        UserAuthRightEntity entRight = (mode == DefAPIAuth.MODE_MOD)
                ? userRightRepository.findByUserIdAndType(user.getId(), DefAPIAuth.AUTH_TYPE_MODIFIABLE)
                : null;

        if (entRight == null) {
            entRight = new UserAuthRightEntity();
            entRight.setUser(user);
        }

        if (user.getId().equals(manId)) return entRight;

        Set<Integer> setRightsMod = (user.getRole().getId() == DefAPIAuth.TYPE_ADMIN)
                ? new HashSet<>(rights)
                : filterRightsByManager(rights, manId);

        if (setRightsMod == null) return null;

        entRight.setAuthRights(setRightsMod.toString());
        entRight.setStatus(SystemConstant.ACTIVE_STATUS);
        entRight.setType(DefAPIAuth.AUTH_TYPE_MODIFIABLE);

        return userRightRepository.save(entRight);
    }

    private Set<Integer> filterRightsByManager(Set<Integer> requestedRights, Long manId) {
        UserAuthRightEntity manRight = userRightRepository.findByUserIdAndType(manId, DefAPIAuth.AUTH_TYPE_MODIFIABLE);
        if (manRight == null) return null;

        Set<Integer> manRightsSet = extractRightsSet(manRight.getAuthRights());

        return requestedRights.stream()
                .filter(manRightsSet::contains)
                .collect(Collectors.toSet());
    }
}
