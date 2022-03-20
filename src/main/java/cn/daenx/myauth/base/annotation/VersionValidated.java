package cn.daenx.myauth.base.annotation;

import java.lang.annotation.*;

/**
 * 版本校验，主要校验vkey是否正确、版本是否停用
 * @author DaenMax
 */
//定义可以在方法和类上使用此注解
@Target({ElementType.METHOD,ElementType.TYPE})
//定义的这个注解是注解，会在class字节码文件中存在，在运行时可以通过反射获取到。
@Retention(RetentionPolicy.RUNTIME)
//定义子类可以继承父类中的该注解
@Inherited

public @interface VersionValidated {
}
