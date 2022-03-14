package cn.myauthx.api.base.annotation;

import java.lang.annotation.*;

//定义可以在方法和类上使用此注解
@Target({ElementType.METHOD, ElementType.TYPE})
//定义的这个注解是注解，会在class字节码文件中存在，在运行时可以通过反射获取到。
@Retention(RetentionPolicy.RUNTIME)
//定义子类可以继承父类中的该注解
@Inherited

public @interface AdminLogin {
    /**
     * 是否强制超级管理员角色，默认强制
     *
     * @return
     */
    boolean is_super_role() default true;

    /**
     * 是否必须账号是：admin，默认为否
     *
     * @return
     */
    boolean is_admin() default false;
}
