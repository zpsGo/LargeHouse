package zps.myrpc;

import java.io.Serializable;

/**
 * @author: zps
 **/
public class RpcRequest implements Serializable {

    private String className;

    private String methodName;

    private Object[] params;
    private String version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getVersion() {

        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
