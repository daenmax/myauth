package cn.daenx.myauth.base.annotation;

import java.lang.annotation.*;

/**
 * 软件校验，主要校验skey是否正确、软件是否停用、是否维护
 * @author DaenMax
 */
//定义可以在方法和类上使用此注解
@Target({ElementType.METHOD,ElementType.TYPE})
//定义的这个注解是注解，会在class字节码文件中存在，在运行时可以通过反射获取到。
@Retention(RetentionPolicy.RUNTIME)
//定义子类可以继承父类中的该注解
@Inherited

public @interface BanValidated {
    boolean is_device_code() default false;
    boolean is_ip() default false;
    //如果要开启验证用户，那么必须同时使用@UserLogin
    boolean is_user() default false;

}
