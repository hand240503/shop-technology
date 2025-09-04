package com.ndh.ShopTechnology.services.common;

import com.ndh.ShopTechnology.def.DefAPIAuth;
import com.ndh.ShopTechnology.entities.user.UserEntity;
import com.ndh.ShopTechnology.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class APIAuthService {

    /*----------------------------------------Work type----------------------------------------*/
    public static final int WORK_GET = 1;
    public static final int WORK_LST = 2;
    public static final int WORK_NEW = 3;
    public static final int WORK_MOD = 4;
    public static final int WORK_DEL = 5;

    /*----------------------------------------Work type----------------------------------------*/

    private final UserRepository userRepository;

    @Autowired
    public APIAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static boolean canAuthorizeWithOneRight(UserEntity user, Integer... right) {
        if (right == null) return true;
        return user.haveAnyRights(right);
    }

    public static boolean canAuthorizeWithOneRight(UserEntity user, Set<Integer> right) {
        if (right == null) return true;
        return user.haveAnyRights(right);
    }

    public static boolean checkUserPermissions(UserEntity ent, int work, Integer... rights) {
        Set<Integer> rLst = new HashSet<>();
        Collections.addAll(rLst, DefAPIAuth.R_ADMIN);

        if (rights != null) {
            Collections.addAll(rLst, rights);
        }

        switch (work) {
            case WORK_GET:
                rLst.add(DefAPIAuth.R_AUT_ALL_GET);
                break;
            case WORK_NEW:
                rLst.add(DefAPIAuth.R_AUT_ALL_NEW);
                break;
            case WORK_MOD:
                rLst.add(DefAPIAuth.R_AUT_ALL_MOD);
                break;
            case WORK_DEL:
                rLst.add(DefAPIAuth.R_AUT_ALL_DEL);
                break;
            default:
                return false;
        }

        return APIAuthService.canAuthorizeWithOneRight(ent, rLst);
    }
}
