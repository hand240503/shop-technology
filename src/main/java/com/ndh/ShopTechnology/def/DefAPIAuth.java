package com.ndh.ShopTechnology.def;

public class DefAPIAuth {

    /*---------------------------------------- Work type ----------------------------------------*/
    public static final int WORK_GET = 1;
    public static final int WORK_LST = 2;
    public static final int WORK_NEW = 3;
    public static final int WORK_MOD = 4;
    public static final int WORK_DEL = 5;
    /*---------------------------------------- Work type ----------------------------------------*/

    /*---------------------------------------- Work mode ----------------------------------------*/
    public static final int MODE_NEW = 1;
    public static final int MODE_MOD = 2;
    /*---------------------------------------- Work mode ----------------------------------------*/

    /*-------------------------------------- Rights for ADMIN --------------------------------------*/
    public static Integer R_ADMIN       = 100;
    public static Integer R_AUT_ALL_GET = 101;
    public static Integer R_AUT_ALL_NEW = 102;
    public static Integer R_AUT_ALL_MOD = 103;
    public static Integer R_AUT_ALL_DEL = 104;
    /*-------------------------------------- Rights for ADMIN --------------------------------------*/

    /*------------------------------------ Rights for USERS Ent -----------------------------------*/
    public static Integer R_AUT_USER_GET = 1000001;
    public static Integer R_AUT_USER_NEW = 1000002;
    public static Integer R_AUT_USER_MOD = 1000003;
    public static Integer R_AUT_USER_DEL = 1000004;
    /*------------------------------------ Rights for USERS Ent -----------------------------------*/

    /*---------------------------- Permissions: Modifiable vs Non-Modifiable ----------------------------*/
    public static final Integer AUTH_TYPE_DEFAULT    = 100;
    public static final Integer AUTH_TYPE_MODIFIABLE = 0;
    /*---------------------------- Permissions: Modifiable vs Non-Modifiable ----------------------------*/

    public static final Long TYPE_USER       = 0L;
    public static final Long TYPE_ADMIN      = 100L;
    public static final Long TYPE_MANAGER    = 101L;
    public static final Long TYPE_EMPLOYEE   = 201L;
    public static final Long TYPE_CUSTOMER   = 202L;
}
