package com.timecat.data.bmob.dao;

import android.text.TextUtils;

import com.timecat.data.bmob.data._User;
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
        final _User user = new _User();
        if (_User.isEmail(username)) {
            user.setEmail(username);
        }
        if (_User.isMobileNO(username)) {
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
        return _User.logIn(username, password, _User.class).subscribe(
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
    public static _User getCurrentUser() {
        return AVUser.getCurrentUser(_User.class);
    }

    /**
     * 用户管理：2.5、查询用户是否已被注册
     */
    public static Disposable queryUsersExits(String username, final EasyRequestUserNull listener) {
        AVQuery<_User> query = new AVQuery<>("_User");
        query.whereEqualTo("username", username);
        AVQuery<_User> queryEmail = new AVQuery<>("_User");
        queryEmail.whereEqualTo("email", username);
        AVQuery<_User> queryPhone = new AVQuery<>("_User");
        queryPhone.whereEqualTo("mobilePhoneNumber", username);
        List<AVQuery<_User>> cons = new ArrayList<>();
        cons.add(query);
        cons.add(queryEmail);
        cons.add(queryPhone);
        return AVQuery.or(cons).getFirstInBackground().subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 用户管理：2.5、查询用户
     */
    public static Disposable queryUsers(String username, final int limit, final EasyRequestUserList listener) {
        AVQuery<_User> query = new AVQuery<>("_User");
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
        AVQuery<_User> query = new AVQuery<>("_User");
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
        AVQuery<_User> query = new AVQuery<>("_User");
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
        return _User.requestEmailVerifyInBackground(email).subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }

    /**
     * 找回密码
     */
    public static Disposable requestPasswordResetByEmail(String email, final EasyRequest listener) {
        return _User.requestPasswordResetInBackground(email)
                    .subscribe(
                            avUser -> listener.getOnSuccess().invoke(avUser),
                            e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
                    );
    }

    /**
     * 找回密码
     */
    public static Disposable requestPasswordResetBySmsCode(String phone, final EasyRequest listener) {
        return _User.requestPasswordResetBySmsCodeInBackground(phone)
                    .subscribe(
                            avUser -> listener.getOnSuccess().invoke(avUser),
                            e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
                    );
    }
    /**
     * 找回密码
     */
    public static Disposable resetPasswordBySmsCode(String smsCode, String newPassword, final EasyRequest listener) {
        return _User.resetPasswordBySmsCodeInBackground(smsCode, newPassword)
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
    public static Observable<_User> login(String phone, String password) {
        return _User.logIn(phone, password, _User.class);
    }

    /**
     * 使用帐号与密码登录.
     *
     * @param phone 登录用户，包含账号与密码
     * @return
     */
    public static Observable<AVUser> signUp(String phone, String password) {
        _User user = new _User();
        user.setUsername(phone);
        if (_User.isEmail(phone)) {
            user.setEmail(phone);
        } else if (_User.isMobileNO(phone)) {
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
    public static Observable<_User> loginBySMS(final String phone, final String smsCode) {
        //转换为Observable对象，方便使用RxJava处理
        return _User.loginBySMSCode(phone, smsCode, _User.class);
    }

    /**
     * 请求短信验证码，发送到指定号码的手机上
     *
     * @param phone
     */
    public static Disposable requestSmsCode(String phone, final EasyRequest listener) {
        return _User.requestLoginSmsCodeInBackground(phone).subscribe(
                avUser -> listener.getOnSuccess().invoke(avUser),
                e -> listener.getOnError().invoke(new DataError(CODE_NULL, e.getMessage()))
        );
    }
    //endregion
}
