package com.gutils.gradle.router.utils

import com.gutils.gradle.router.core.RegisterTransform
import org.gradle.internal.impldep.org.apache.http.util.TextUtils
import org.gradle.util.TextUtil
import org.objectweb.asm.*

import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * Scan all class in the package: com/gome/router/
 * find out all routers,interceptors and providers
 * @author qiudongchao
 */
class ScanUtil {

    /**
     * scan jar file
     * @param jarFile All jar files that are compiled into apk
     * @param destFile dest file after this transform
     */
    static void scanJar(File jarFile, File destFile) {
        if (jarFile) {
            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                if (containsTargetPackage(entryName)) {
                    InputStream inputStream = file.getInputStream(jarEntry)
                    scanClass(inputStream)
                    inputStream.close()
                } else if (ScanSetting.GENERATE_TO_CLASS_FILE_NAME == entryName) {
                    // mark this jar file contains LogisticsCenter.class
                    // After the scan is complete, we will generate register code into this file
                    RegisterTransform.fileContainsInitClass = destFile
                }
            }
            file.close()
        }
    }

    private static boolean containsTargetPackage(String entryName) {
        if (entryName == null || !entryName.endsWith(".class")) {
            return false
        }
        String currentEntryName = entryName.replaceAll("/", ".")
        for (String packageName : ScanSetting.TARGET_LIST) {
            if (currentEntryName.contains(packageName)) {
                return true
            }
        }
        return entryName.startsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)
    }

    static boolean shouldProcessPreDexJar(String path) {
        for (String packageName in ScanSetting.FILTER_LIST) {
            if (packageName != null && path.contains(packageName)) {
                return false
            }
        }
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    static boolean shouldProcessClass(String entryName) {
        if (entryName == null || !entryName.endsWith(".class")) {
            return false
        }
        String currentEntryName = entryName.replaceAll("/", ".")
        for (String packageName in ScanSetting.TARGET_LIST) {
            if (packageName != null && packageName.contains(currentEntryName)) {
                return true
            }
        }
        return entryName != null && entryName.startsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)
    }

    /**
     * scan class file
     * @param class file
     */
    static void scanClass(File file) {
        scanClass(new FileInputStream(file))
    }

    static void scanClass(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ScanClassVisitor cv = new ScanClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        inputStream.close()
    }

    static class ScanClassVisitor extends ClassVisitor {
        String className

        ScanClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        @Override
        AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            RegisterTransform.registerList.each { ext ->
                if (ext.interfaceName && desc.contains(ext.interfaceName)) {
                    ext.classList.add(className)
                }
            }
            return super.visitAnnotation(desc, visible)
        }

        @Override
        void visit(int version, int access, String name, String signature,
                   String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
            className = name
        }
    }

}