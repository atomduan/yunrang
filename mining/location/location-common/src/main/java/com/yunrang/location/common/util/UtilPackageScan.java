package com.yunrang.location.common.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UtilPackageScan {
	private static Logger Log = LoggerFactory.getLogger(UtilPackageScan.class);
	
	public static Class<?> getFirstMatchClass(String rootPack, String className) throws ClassNotFoundException {
		Set<Class<?>> classes = getClasses(rootPack);
		for (Class<?> clz : classes) {
			if (className.trim().equals(clz.getName())) {
				return clz;
			}
		} 
		throw new ClassNotFoundException("packageScaner can not find class ["+className + "] under pacakage ["+rootPack+"]");
	}
	
	public static List<Class<?>> getMatchedBizClassList(String rootPack, Class<?> filter) {
		Set<Class<?>> classes = getClasses(rootPack);
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> clz : classes) {
			if (filter.isAssignableFrom(clz)) {
				if (!Modifier.isInterface(clz.getModifiers())) {
					if (!Modifier.isAbstract(clz.getModifiers())){
						list.add(clz);
					}
				}
			}
		} 
		return list;
	}
	
	private static Set<Class<?>> getClasses(String pack) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					JarFile jar;
					try {
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							if (name.charAt(0) == '/') {
								name = name.substring(1);
							}
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								if (idx != -1) {
									packageName = name.substring(0, idx).replace('/', '.');
								}
								if ((idx != -1) || recursive) {
									if (name.endsWith(".class") && !entry.isDirectory()) {
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											classes.add(Class.forName(packageName + '.'+ className));
										} catch (ClassNotFoundException e) {
											Log.error("PackageScaner exception A : ", e);
										}
									}
								}
							}
						}
					} catch (IOException e) {
						Log.error("PackageScaner exception B : ", e);
					}
				}
			}
		} catch (IOException e) {
			Log.error("PackageScaner exception C: ", e);
		}
		return classes;
	}

	private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
			} else {
				findAndAddClassesInPackageByFile(packageName + "."+ file.getName(), file.getAbsolutePath(), recursive,classes);
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					Log.error("PackageScaner exception D : ", e);
				}
			}
		}
	}
	

    
    public static String simpleClassNameProcess(String clzName) {
        if (clzName != null && clzName.length()>0) {
            clzName = clzName.trim();
            String[] eles = clzName.split("\\.");
            String simpleName = eles[eles.length-1];
            if (Pattern.matches("[a-zA-Z]+", simpleName)) {
                return simpleName;
            }
        }
        throw new IllegalArgumentException("["+clzName+"] is not a simple class name, trim the suffix such like ; or ^ and so on....?");
    }
}
