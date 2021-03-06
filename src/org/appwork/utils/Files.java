/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;

import org.appwork.utils.os.CrossSystem;

public class Files {
    /**
     * delete all files/folders that are given
     * 
     * @param files
     * @throws IOException
     */
    public static void deleteRecursiv(final File... files) throws IOException {
        final ArrayList<File> ret = Files.getFiles(true, true, files);
        for (int i = ret.size() - 1; i >= 0; i--) {
            final File file = ret.get(i);
            if (!file.exists() || file.isFile()) {
                ret.remove(i);
            }
            if (file.exists() && file.isFile() && !file.delete()) { throw new IOException("could not delete " + file); }

        }
        for (int i = ret.size() - 1; i >= 0; i--) {
            final File file = ret.get(i);
            if (file.isDirectory()) {
                ret.remove(i);
            }
            if (file.exists() && file.isDirectory() && !file.delete()) { throw new IOException("could not delete " + file); }
        }
    }

    public static LinkedList<String> getDirectories_NonRecursive(final File startDirectory, final boolean includeStart) throws IOException {
        final LinkedList<String> done = new LinkedList<String>();
        File current = null;
        File[] currents = null;
        final ArrayList<File> todo = new ArrayList<File>();
        todo.add(startDirectory);
        while (todo.size() > 0) {
            current = todo.remove(0);
            currents = current.listFiles();
            done.add(current.getCanonicalPath());
            if (currents != null) {
                for (int index = currents.length - 1; index >= 0; index--) {
                    if (currents[index].isDirectory()) {
                        final String temp = currents[index].getCanonicalPath();
                        if (!done.contains(temp)) {
                            todo.add(currents[index]);
                        }
                    }
                }
            }
        }
        /* remove startdirectory if wished */
        if (!includeStart && done.size() > 0) {
            done.remove(0);
        }
        return done;
    }

    /**
     * returns File if it exists (case (In)Sensitive). returns null if file does
     * not exist
     */
    public static File getExistingFile(final File file, final boolean caseSensitive) {
        if (file == null) { return null; }
        if (caseSensitive) {
            if (file.exists()) { return file; }
            return null;
        }
        /* get list of files in current directory */
        final String lowerCaseFileName = file.getName().toLowerCase();
        final File parent = file.getParentFile();
        if (parent != null) {
            final File[] list = parent.listFiles();
            if (list != null) {
                for (final File ret : list) {
                    if (ret.getName().equalsIgnoreCase(lowerCaseFileName)) { return ret; }
                }
            }
        }
        return null;
    }

    /**
     * Returns the fileextension for a file with the given name
     * 
     * @param name
     * @return
     */
    public static String getExtension(final String name) {
        if (name == null) { return null; }
        final int index = name.lastIndexOf(".");
        if (index < 0) { return null; }
        return name.substring(index + 1).toLowerCase();
    }

    /**
     * return all files ( and folders if includeDirectories is true ) for the
     * given files
     * 
     * @param includeDirectories
     * @param files
     * @return
     */
    public static ArrayList<File> getFiles(final boolean includeDirectories, final boolean includeFiles, final File... files) {
        final ArrayList<File> ret = new ArrayList<File>();
        if (files != null) {
            for (final File f : files) {
                if (!f.exists()) {
                    continue;
                }
                if (f.isDirectory()) {
                    if (includeDirectories) {
                        ret.add(f);
                    }
                    ret.addAll(Files.getFiles(includeDirectories, includeFiles, f.listFiles()));
                } else if (includeFiles) {
                    ret.add(f);
                }
            }
        }
        return ret;
    }

    /**
     * Returns the mikmetype of the file. If unknown, it returns
     * Unknown/extension
     * 
     * @param name
     * @return
     */
    public static String getMimeType(final String name) {
        if (name == null) { return null; }
        final FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String ret = fileNameMap.getContentTypeFor(name);
        if (ret == null) {
            ret = "unknown/" + Files.getExtension(name);
        }
        return ret;
    }

    /**
     * Returns the relative path of file based on root.
     * 
     * @param root
     * @param f
     * @return
     */
    public static String getRelativePath(final File root, final File file) {
        final String rootPath, filePath;
        if (CrossSystem.isWindows()) {
            rootPath = root.getAbsolutePath().toLowerCase();
            filePath = file.getAbsolutePath().toLowerCase();
        } else {
            rootPath = root.getAbsolutePath();
            filePath = file.getAbsolutePath();
        }

        if (!filePath.startsWith(rootPath)) { return null; }
        if (rootPath.equals(filePath)) { return "/"; }
        return file.getAbsolutePath().substring(rootPath.length() + 1);
    }

    public static void main(final String[] args) {
        System.out.println(Files.getRelativePath(new File("C:/Test/"), new File("c:/test/eins/zwei/drei.vier")));
    }
}
