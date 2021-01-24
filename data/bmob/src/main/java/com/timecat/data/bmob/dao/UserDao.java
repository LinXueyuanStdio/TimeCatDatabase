package com.timecat.data.bmob.dao;

import android.text.TextUtils;

import com.jess.arms.utils.LogUtils;
import com.timecat.data.bmob.StaticKt;
import com.timecat.data.bmob.data.User;
import com.timecat.data.bmob.ext.bmob.EasyRequest;
import com.timecat.data.bmob.ext.bmob.EasyRequestUser;
import com.timecat.data.bmob.ext.bmob.EasyRequestUserList;
import com.timecat.data.bmob.ext.bmob.EasyRequestUserNull;
import com.timecat.identity.data.service.DataError;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/3/20
 * @description null
 * @usage null
 */
public class UserDao extends BaseModel {

    private static UserDao ourInstance = new UserDao();

    public static UserDao getInstance() {
        return ourInstance;
    }

    private UserDao() {
    }

    //region 登录注册、验证邮箱、找回密码

    /**
     * 用户管理：2.1、注册
     */
    public static Disposable register(String username, String password, final EasyRequestUser listener) {
        if (TextUtils.isEmpty(username)) {
            listener.getOnError().invoke(new DataError(CODE_NULL, "请填写用户名"));
            return null;
        }
        if (TextUtils.isEmpty(password)) {
            listener.getOnError().invoke(new DataError(CODE_NULL, "请填写密码"));
            return null;
        }
        final User user = new User();
        if (StaticKt.isEmail(username)) {
            user.setEmail(username);
        }
        if (StaticKt.isMobileNO(username)) {
            user.setMobilePhoneNumber(username);
        }
        user.setUsername(username);
        user.setPassword(password);
        return user.signUpInBackground().subscribe(
                avUser -> listener.getOnSuccess().invoke(user),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 用户管理：2.2、登录
     */
    public static Disposable login(String username, String password, final EasyRequestUser listener) {
        if (TextUtils.isEmpty(username)) {
            listener.getOnError().invoke(new DataError(CODE_NULL, "请填写用户名"));
            return null;
        }
        if (TextUtils.isEmpty(password)) {
            listener.getOnError().invoke(new DataError(CODE_NULL, "请填写密码"));
            return null;
        }
        return User.logIn(username, password, User.class).subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 用户管理：2.3、退出登录
     */
    public static void logout() {
        AVUser.logOut();
    }

    /**
     * 用户管理：2.4、获取当前用户
     */
    @Nullable
    public static User getCurrentUser() {
        return AVUser.getCurrentUser(User.class);
    }

    /**
     * 用户管理：2.5、查询用户是否已被注册
     */
    public static Disposable queryUsersExits(String username, final EasyRequestUserNull listener) {
        AVQuery<User> query = User.getUserQuery(User.class);
        query.whereEqualTo("username", username);
        AVQuery<User> queryEmail = User.getUserQuery(User.class);
        queryEmail.whereEqualTo("email", username);
        AVQuery<User> queryPhone = User.getUserQuery(User.class);
        queryPhone.whereEqualTo("mobilePhoneNumber", username);

        List<AVQuery<User>> cons = new ArrayList<>();
        cons.add(query);
        cons.add(queryEmail);
        cons.add(queryPhone);
        return AVQuery.or(cons).getFirstInBackground().subscribe(
                avUser -> {
                    LogUtils.debugInfo("a", avUser + "");
                    listener.getOnSuccess().invoke(avUser);
                },
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage())),
                () -> {
                    LogUtils.debugInfo("a", "complete");
                    listener.getOnComplete().invoke();
                }
        );
    }

    /**
     * 用户管理：2.5、查询用户
     */
    public static Disposable queryUsers(String username, final int limit, final EasyRequestUserList listener) {
        AVQuery<User> query = User.getUserQuery(User.class);
        query.whereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        return query.findInBackground().subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 用户管理：2.5、查询用户邮箱是否存在，list == null && e == null 则存在且唯一
     */
    public static Disposable queryEmail(String email, final int limit, final EasyRequestUserList listener) {
        AVQuery<User> query = User.getUserQuery(User.class);
        query.whereEqualTo("email", email);
        query.setLimit(limit);
        query.order("-createdAt");
        return query.findInBackground().subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 用户管理：2.5、查询用户手机号是否存在，list == null && e == null 则存在且唯一
     */
    public static Disposable queryPhone(String phone, final int limit, final EasyRequestUserList listener) {
        AVQuery<User> query = User.getUserQuery(User.class);
        query.whereEqualTo("mobilePhoneNumber", phone);
        query.setLimit(limit);
        query.order("-createdAt");
        return query.findInBackground().subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 验证邮箱
     */
    public static Disposable requestEmailVerify(String email, final EasyRequest listener) {
        return User.requestEmailVerifyInBackground(email).subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 找回密码
     */
    public static Disposable requestPasswordResetByEmail(String email, final EasyRequest listener) {
        return User.requestPasswordResetInBackground(email)
                   .subscribe(
                            avUser -> listener.getOnSuccess().invoke(avUser),
                            e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
                    );
    }

    /**
     * 找回密码
     */
    public static Disposable requestPasswordResetBySmsCode(String phone, final EasyRequest listener) {
        return User.requestPasswordResetBySmsCodeInBackground(phone)
                   .subscribe(
                            avUser -> listener.getOnSuccess().invoke(avUser),
                            e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
                    );
    }

    /**
     * 找回密码
     */
    public static Disposable resetPasswordBySmsCode(String smsCode, String newPassword, final EasyRequest listener) {
        return User.resetPasswordBySmsCodeInBackground(smsCode, newPassword)
                   .subscribe(
                            avUser -> listener.getOnSuccess().invoke(avUser),
                            e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
                    );
    }

    /**
     * 使用帐号与密码登录.
     *
     * @param phone 登录用户，包含账号与密码
     * @return
     */
    public static Observable<User> login(String phone, String password) {
        return User.logIn(phone, password, User.class);
    }

    /**
     * 使用帐号与密码登录.
     *
     * @param phone 登录用户，包含账号与密码
     * @return
     */
    public static Observable<AVUser> signUp(String phone, String password) {
        User user = new User();
        user.setUsername(phone);
        if (StaticKt.isEmail(phone)) {
            user.setEmail(phone);
        } else if (StaticKt.isMobileNO(phone)) {
            user.setMobilePhoneNumber(phone);
        }
        user.setPassword(password);
        return user.signUpInBackground();
    }

    /**
     * 使用短信验证码登录
     *
     * @param phone
     * @param smsCode
     * @return
     */
    public static Observable<User> loginBySMS(final String phone, final String smsCode) {
        //转换为Observable对象，方便使用RxJava处理
        return User.loginBySMSCode(phone, smsCode, User.class);
    }

    /**
     * 请求短信验证码，发送到指定号码的手机上
     *
     * @param phone
     */
    public static Disposable requestSmsCode(String phone, final EasyRequest listener) {
        return User.requestLoginSmsCodeInBackground(phone).subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }
    //endregion
}
