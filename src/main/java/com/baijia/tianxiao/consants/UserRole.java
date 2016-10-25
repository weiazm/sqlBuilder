package com.baijia.tianxiao.consants;

/**
 * 平台用户角色
 */
public enum UserRole {

    ORGANIZATION(6, "机构"),

    TEACHER(0, "老师"),

    STUDENT(2, "学生");

    private UserRole(int role, String label) {
        this.role = role;
        this.label = label;
    }

    private int role;

    private String label;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static String getLabelByRole(int role) {
        for (UserRole ur : UserRole.values()) {
            if (ur.getRole() == role) {
                return ur.getLabel();
            }
        }
        return "未知来源";
    }
}
