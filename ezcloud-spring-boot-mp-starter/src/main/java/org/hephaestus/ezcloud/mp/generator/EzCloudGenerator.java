/*
 * MIT License
 *
 * Copyright (c) 2020 CloudSen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.hephaestus.ezcloud.mp.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hephaestus.ezcloud.autoconfigure.exception.BusinessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 代码生成器二次封装
 * <p>父包名（默认） = cn.uni.项目名</p>
 * <p>DO包名 = 父包名.entity</p>
 * <p>mvc包名 = 父包名.模块名.业务域名.mvc层</p>
 *
 * @author clouds3n
 * @date 2021-12-10
 */
public class EzCloudGenerator {

    /**
     * 生成代码
     *
     * @param property 生成器参数
     */
    public static void generate(GeneratorProperty property) {
        checkProperty(property);
        String parentPackage = property.getParentPackage();
        String packagePrefix = property.getPackagePrefix();
        String projectName = property.getProjectName();
        Scanner sc = new Scanner(System.in);
        System.out.println("> 请输入作者名称:");
        String author = Optional.ofNullable(sc.nextLine()).filter(StringUtils::isNotBlank).orElseThrow(() -> new BusinessException("作者名不能为空"));
        System.out.println("> 请输入业务模块名");
        String businessDomain = Optional.ofNullable(sc.nextLine()).filter(StringUtils::isNotBlank).orElseThrow(() -> new BusinessException("业务模块名不能为空"));
        property.setBusinessDomain(businessDomain);
        FastAutoGenerator.create(property.getDataSourceBuilder())
            // 全局配置
            .globalConfig((scanner, builder) -> {
                builder.author(author).disableOpenDir();
                if (property.getOverwriteFiles() != null && property.getOverwriteFiles()) {
                    builder.fileOverride();
                }
            })
            // 包配置
            .packageConfig((scanner, builder) -> builder.parent(StringUtils.isNotBlank(parentPackage) ? parentPackage : packagePrefix + "." + projectName.toLowerCase().replaceAll("-", ""))
                .service("business." + businessDomain + ".service")
                .serviceImpl("business." + businessDomain + ".service.impl")
                .mapper("business." + businessDomain + ".mapper")
                .controller("web." + businessDomain)
                .pathInfo(buildPathInfo(property))
            )
            // 策略配置
            .strategyConfig((scanner, builder) -> {
                builder.addInclude(getTables(scanner.apply("> 请输入表名，多个英文逗号分隔，所有输入 all:")))
                    .controllerBuilder().enableRestStyle().enableHyphenStyle()
                    .serviceBuilder().formatServiceFileName("%sService")
                    .entityBuilder().enableLombok().enableChainModel().formatFileName("%sDO")
                    .enableRemoveIsPrefix().enableTableFieldAnnotation()
                    .logicDeleteColumnName("deleted").versionColumnName("version");
                if (property.getEntitySuperClass() != null) {
                    builder.entityBuilder().superClass(property.getEntitySuperClass());
                }
                builder.entityBuilder().addTableFills(
                    new Property("gmt_create", FieldFill.INSERT),
                    new Property("gmt_modified", FieldFill.INSERT_UPDATE)
                ).build();
            })
            .templateEngine(new FreemarkerTemplateEngine())
            .templateConfig((string, builder) -> builder.entity("tpl/entity.java").controller("tpl/controller.java").serviceImpl("tpl/serviceImpl.java"))
            .injectionConfig(builder -> builder.customMap(new HashMap<>(1){{
                put("businessDomain", businessDomain);
            }}))
            .execute();
        createMustDirectories(property);
    }

    /**
     * 自定义文件生成路径
     *
     * @return 文件对应的路径map
     */
    @SuppressWarnings("DuplicatedCode")
    public static Map<OutputFile, String> buildPathInfo(GeneratorProperty property) {
        Map<OutputFile, String> pathInfo = new HashMap<>(16);
        String userDir = System.getProperty("user.dir");
        String projectName = property.getProjectName();
        String businessDomain = property.getBusinessDomain();
        String packagePathPrefix = property.getPackagePrefix().replaceAll("\\.", File.separator);
        String modulePathPrefix = userDir + File.separator + projectName;
        String businessModuleJavaPath = modulePathPrefix + "-business" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String businessModuleResourcePath = modulePathPrefix + "-business" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        String webModulePath = modulePathPrefix + "-web" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String entityModulePath = modulePathPrefix + "-entity" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String businessPkgPath = packagePathPrefix + File.separator + projectName.replaceAll("-", "") + File.separator + "business" + File.separator + businessDomain + File.separator;
        String webPkgPath = packagePathPrefix + File.separator + projectName.replaceAll("-", "") + File.separator + "web" + File.separator + businessDomain + File.separator;
        String entityPkgPath = packagePathPrefix + File.separator + projectName.replaceAll("-", "") + File.separator + "entity" + File.separator;
        String mapperPath = businessModuleJavaPath + businessPkgPath + "mapper";
        String mapperXmlPath = businessModuleResourcePath + "mapper";
        String servicePath = businessModuleJavaPath + businessPkgPath + "service";
        String serviceImplPath = businessModuleJavaPath + businessPkgPath + "service" + File.separator + "impl";
        String controllerPath = webModulePath + webPkgPath;
        String entityPath = entityModulePath + entityPkgPath;
        pathInfo.put(OutputFile.controller, controllerPath);
        pathInfo.put(OutputFile.service, servicePath);
        pathInfo.put(OutputFile.serviceImpl, serviceImplPath);
        pathInfo.put(OutputFile.entity, entityPath);
        pathInfo.put(OutputFile.mapper, mapperPath);
        pathInfo.put(OutputFile.mapperXml, mapperXmlPath);
        return pathInfo;
    }

    @SuppressWarnings("DuplicatedCode")
    public static void createMustDirectories(GeneratorProperty property) {
        String userDir = System.getProperty("user.dir");
        String projectName = property.getProjectName();
        String businessDomain = property.getBusinessDomain();
        String packagePathPrefix = property.getPackagePrefix().replaceAll("\\.", File.separator);
        String modulePathPrefix = userDir + File.separator + projectName;
        String businessModuleJavaPath = modulePathPrefix + "-business" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String businessPkgPath = packagePathPrefix + File.separator + projectName.replaceAll("-", "") + File.separator + "business" + File.separator + businessDomain + File.separator;
        String businessPath = businessModuleJavaPath + businessPkgPath;

        String modelDirPath = businessPath + "model" + File.separator;
        String dtoDirPath = businessPath + "model" + File.separator + "dto" + File.separator;
        String voDirPath = businessPath + "model" + File.separator + "vo" + File.separator;
        String enumsDirPath = businessPath + "model" + File.separator + "enums" + File.separator;
        String converterDirPath = businessPath + "converter" + File.separator;

        String modelDirKeepFile = modelDirPath + ".gitkeep";
        String dtoDirKeepFile = dtoDirPath+ ".gitkeep";
        String voDirKeepFile = voDirPath + ".gitkeep";
        String enumsDirKeepFile = enumsDirPath + ".gitkeep";
        String converterDirKeepFile = converterDirPath + ".gitkeep";

        createFileOrPath(Path.of(modelDirKeepFile));
        createFileOrPath(Path.of(dtoDirKeepFile));
        createFileOrPath(Path.of(voDirKeepFile));
        createFileOrPath(Path.of(enumsDirKeepFile));
        createFileOrPath(Path.of(converterDirKeepFile));
    }

    private static void createFileOrPath(Path path) {
        if (path == null) {
            return;
        }
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkProperty(GeneratorProperty property) {
        if (property.getDataSourceBuilder() == null) {
            throw new BusinessException("缺少数据库配置");
        }
        if (StringUtils.isBlank(property.getProjectName())) {
            throw new BusinessException("缺少项目名");
        }
    }

    /**
     * 获取表名
     *
     * @param tables 逗号分割的表名，可以为"all"
     * @return 表名列表，如果为"all"，则返回空列表
     */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneratorProperty {
        private DataSourceConfig.Builder dataSourceBuilder;
        private String packagePrefix = "cn.uni.";
        private String projectName;
        private String parentPackage;
        private String businessDomain;
        private Boolean overwriteFiles;
        private Class<?> entitySuperClass;
    }
}
