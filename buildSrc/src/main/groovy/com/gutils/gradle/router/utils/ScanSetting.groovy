package com.gutils.gradle.router.utils
/**
 * register setting
 * @author qiudongchao
 */
class ScanSetting {
    static final String PLUGIN_NAME = "GRouter"
    /**
     * The register code is generated into this class
     */
    static final String GENERATE_TO_CLASS_NAME = 'com/gome/mobile/frame/router/GRouter'
    /**
     * you know. this is the class file(or entry in jar file) name
     */
    static final String GENERATE_TO_CLASS_FILE_NAME = GENERATE_TO_CLASS_NAME + '.class'
    /**
     * The register code is generated into this method
     */
    static final String GENERATE_TO_METHOD_NAME = 'init'
    /**
     * The package name of the class generated by the annotationProcessor
     */
    static final String ROUTER_CLASS_PACKAGE_NAME = 'cn/gome/staff/'
    /**
     * The package name of the interfaces
     */
    private static final INTERFACE_PACKAGE_NAME = 'com/gome/mobile/frame/router/annotation/'

    /**
     * register method name in class: {@link #GENERATE_TO_CLASS_NAME}
     */
    static final String REGISTER_METHOD_NAME = 'register'

    /**
     * 需要扫描的包名列表
     */
    static ArrayList<String> TARGET_LIST = new ArrayList<>();

    /**
     * 需要过滤的包名列表
     */
    static ArrayList<String> FILTER_LIST = new ArrayList<>()

    /**
     * scan for classes which implements this interface
     */
    String interfaceName = ''

    /**
     * jar file which contains class: {@link #GENERATE_TO_CLASS_NAME}
     */
    File fileContainsInitClass
    /**
     * scan result for {@link #interfaceName}
     * class names in this list
     */
    ArrayList<String> classList = new ArrayList<>()

    /**
     * constructor for arouter-auto-register settings
     * @param interfaceName interface to scan
     */
    ScanSetting(String interfaceName) {
        this.interfaceName = INTERFACE_PACKAGE_NAME + interfaceName
    }

}