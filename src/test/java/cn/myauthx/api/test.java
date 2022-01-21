package cn.myauthx.api;
import org.junit.jupiter.api.Test;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

public class test {
    @Test
    public void test(){
        String jsStr =  "function sum(c1,c2){\n" +
                "\treturn c1+c2;\n" +
                "}";
        String ret = "";

        System.out.println(runJs(jsStr,"sum", "a",null));
    }

    /**
     * 执行JS函数，参数和返回值都是String类型
     * @param jsStr
     * @param func
     * @param parameter
     * @return
     */
    public String runJs(String jsStr,String func,String...parameter){
        String regular = jsStr;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");  //创建引擎实例
        Object result = "";
        try {
            engine.eval(regular); //编译
            if (engine instanceof Invocable) {
                result = ((Invocable) engine).invokeFunction(func, parameter); // 执行方法
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "表达式runtime错误:" + e.getMessage();
        }
        return "";
    }

}
