package com.neotys.neoload.model.readers.jmeter;

import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.StringTokenizer;

public final class ClasspathUtils {

    private static final String JAVA_CLASS_PATH = "java.class.path";

    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathUtils.class);

    private ClasspathUtils() {
    }

    public static void updateClassLoader() throws UpdateClassLoaderExceptionWrapper {
        updatePath("search_paths", ";");
        updatePath("user.classpath", File.pathSeparator);
        updatePath("plugin_dependency_paths", ";");
    }

    private static void addPath(final String path) throws UpdateClassLoaderExceptionWrapper {
        File file = new File(path);

        if (file.isDirectory() && !path.endsWith(File.separator)) {
            file = new File(path + File.separator);
        }

        try {
            addURLToSystemClassLoader(file.toURI().toURL());
        } catch (IntrospectionException | MalformedURLException e) {
            throw new UpdateClassLoaderExceptionWrapper(e);
        }

        final StringBuilder sb = new StringBuilder(System.getProperty(JAVA_CLASS_PATH));
        sb.append(File.pathSeparator);
        sb.append(path);

        final File[] jars = listJars(file);
        for (File jar : jars) {
            try {
                addURLToSystemClassLoader(jar.toURI().toURL());
            } catch (IntrospectionException | MalformedURLException e) {
                throw new UpdateClassLoaderExceptionWrapper(e);
            }
            sb.append(File.pathSeparator);
            sb.append(jar.getPath());
        }

        // ClassFinder needs this
        System.setProperty(JAVA_CLASS_PATH, sb.toString());
    }

    private static File[] listJars(final File dir) {
        if (dir.isDirectory()) {
            return dir.listFiles((f, name) -> {
                if (name.endsWith(".jar")) {
                    final File jar = new File(f, name);
                    return jar.isFile() && jar.canRead();
                }
                return false;
            });
        }
        return new File[0];
    }

    private static void updatePath(final String property, final String sep) throws UpdateClassLoaderExceptionWrapper {
        final String userpath = JMeterUtils.getPropDefault(property, "");
        if (userpath.length() <= 0) {
            return;
        }
        LOGGER.info("{}={}", property, userpath);
        StringTokenizer tok = new StringTokenizer(userpath, sep);
        while (tok.hasMoreTokens()) {
            String path = tok.nextToken();
            File f = new File(path);
            if (!f.canRead() && !f.isDirectory()) {
                LOGGER.warn("Can't read {}", path);
            } else {
                LOGGER.info("Adding to loader: {}", path);
                addPath(path);
            }
        }
    }

    private static void addURLToSystemClassLoader(final URL url) throws IntrospectionException {
        final URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        final Class<URLClassLoader> classLoaderClass = URLClassLoader.class;

        try {
            final Class[] parameterTypes = new Class[] { URL.class };
            final Method method = classLoaderClass.getDeclaredMethod("addURL", parameterTypes);
            method.setAccessible(true);
            final Object[] args = new Object[] { url };
            method.invoke(systemClassLoader, args);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            throw new IntrospectionException("Error when adding url to system ClassLoader");
        }
    }
}

class UpdateClassLoaderExceptionWrapper extends Exception {

    public UpdateClassLoaderExceptionWrapper(final Exception e) {
        super(e);
    }
}