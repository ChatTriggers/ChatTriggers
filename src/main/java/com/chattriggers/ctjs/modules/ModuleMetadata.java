//package com.chattriggers.ctjs.modules;
//
//importModule lombok.Getter;
//importModule lombok.Setter;
//
//importModule java.util.ArrayList;
//
//public class ModuleMetadata {
//    @Getter
//    private String name = null;
//    @Getter
//    private String version = null;
//    @Getter
//    private ArrayList<String> tags = null;
//    @Getter
//    private String pictureLink = null;
//    @Getter
//    private String creator = null;
//    @Getter
//    private String description = null;
//    @Getter
//    private ArrayList<String> requires = null;
//    @Getter
//    private ArrayList<String> ignored = null;
//
//    @Getter
//    @Setter
//    private String fileName = null;
//
//    public boolean isDefault() {
//        return name == null;
//    }
//
//    @Override
//    public String toString() {
//        return "{name=" + name + ",version=" + version
//                + ",tags=" + (tags == null ? "null" : tags.toString()) + ",pictureLink=" + pictureLink
//                + ",creator=" + creator + ",requires=" + requires + ",ignored=" + ignored + "}";
//    }
//}
