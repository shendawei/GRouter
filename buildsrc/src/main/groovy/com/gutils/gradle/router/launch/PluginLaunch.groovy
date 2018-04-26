package com.gutils.gradle.router.launch

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.gutils.gradle.router.core.RegisterTransform
import com.gutils.gradle.router.utils.Logger
import com.gutils.gradle.router.utils.ScanSetting
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Simple version of AutoRegister plugin for ARouter
 * @author qiudongchao
 */
public class PluginLaunch implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        def isApp = project.plugins.hasPlugin(AppPlugin)
        //only application module needs this plugin to generate register code
        if (isApp) {
            Logger.make(project)

            Logger.i('Project enable arouter-register plugin')

            def android = project.extensions.getByType(AppExtension)

            def transformImpl = new RegisterTransform(project)

            //init arouter-auto-register settings
            ArrayList<ScanSetting> list = new ArrayList<>(1)
            list.add(new ScanSetting('IRouter'))
            RegisterTransform.registerList = list
            //register this plugin
            android.registerTransform(transformImpl)
        }
    }

}
