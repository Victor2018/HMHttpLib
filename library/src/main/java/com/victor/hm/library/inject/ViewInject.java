package com.victor.hm.library.inject;

import com.victor.hm.library.annotation.BindView;
import com.victor.hm.library.annotation.OnClick;
import com.victor.hm.library.util.Loger;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewInject {
    private static String TAG = "ViewInject";

    /**
     * 解析注解view id
     */
    public static void injectView(Ability ability) {
        Class<?> clazz = ability.getClass();
        Field[] fields = clazz.getDeclaredFields();//获得声明的成员变量
        for (Field field : fields) {
            //判断是否有注解
            try {
                if (field.getAnnotations() != null) {
                    if (field.isAnnotationPresent(BindView.class)) {//如果属于这个注解
                        //为这个控件设置属性
                        field.setAccessible(true);//允许修改反射属性
                        BindView inject = field.getAnnotation(BindView.class);
                        field.set(ability, ability.findComponentById(inject.value()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Loger.e(TAG, "not found view id!");
            }
        }
        injectEvent(ability);
    }
    public static void injectView(AbilitySlice ability) {
        Class<?> clazz = ability.getClass();
        Field[] fields = clazz.getDeclaredFields();//获得声明的成员变量
        for (Field field : fields) {
            //判断是否有注解
            try {
                if (field.getAnnotations() != null) {
                    if (field.isAnnotationPresent(BindView.class)) {//如果属于这个注解
                        //为这个控件设置属性
                        field.setAccessible(true);//允许修改反射属性
                        BindView inject = field.getAnnotation(BindView.class);
                        field.set(ability, ability.findComponentById(inject.value()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Loger.e(TAG, "not found view id!");
            }
        }
        injectEvent(ability);
    }

    private static void injectEvent(final Ability ability) {
        Class cls = ability.getClass();
        Method[] methods = cls.getDeclaredMethods();
        for (final Method m : methods) {
            OnClick click = m.getAnnotation(OnClick.class);//通过反射api获取方法上面的注解
            if (click != null) {
                if (click.value() == -1) return;
                Component view = ability.findComponentById(click.value());//通过注解的值获取View控件
                if (view == null) return;
                view.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        try {
                            m.invoke(ability);//通过反射来调用被注解修饰的方法
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private static void injectEvent(final AbilitySlice ability) {
        Class cls = ability.getClass();
        Method[] methods = cls.getDeclaredMethods();
        for (final Method m : methods) {
            OnClick click = m.getAnnotation(OnClick.class);//通过反射api获取方法上面的注解
            if (click != null) {
                if (click.value() == -1) return;
                Component view = ability.findComponentById(click.value());//通过注解的值获取View控件
                if (view == null) return;
                view.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        try {
                            m.invoke(ability);//通过反射来调用被注解修饰的方法
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

}
