/**
 * Copyright (c) 2009 - 2010 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.zip
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.appwork.utils.Hash;

/**
 * @author daniel
 * 
 */
public class ZipIOWriter {

    private ZipOutputStream zipStream  = null;
    private OutputStream    fileStream = null;
    private File            zipFile    = null;

    private final byte[]    buf        = new byte[8192];

    public ZipIOWriter(final ByteArrayOutputStream stream) throws FileNotFoundException, ZipIOException {
        fileStream = stream;
        zipStream = new ZipOutputStream(fileStream);
    }

    /**
     * constructor for ZipIOWriter
     * 
     * @param zipFile
     *            zipFile we want create (does not overwrite existing files!)
     * @throws FileNotFoundException
     * @throws ZipIOException
     */
    public ZipIOWriter(final File zipFile) throws FileNotFoundException, ZipIOException {
        this.zipFile = zipFile;
        openZip(false);
    }

    /**
     * constructor for ZipIOWriter
     * 
     * @param zipFile
     *            zipFile we want create
     * @param overwrite
     *            overwrite existing ziFiles?
     * @throws FileNotFoundException
     * @throws ZipIOException
     */
    public ZipIOWriter(final File zipFile, final boolean overwrite) throws FileNotFoundException, ZipIOException {
        this.zipFile = zipFile;
        openZip(overwrite);
    }

    /**
     * add given File (File or Directory) to this ZipFile
     * 
     * @param add
     *            File to add
     * @param compress
     *            compress or store
     * @param path
     *            customized path
     * @throws ZipIOException
     * @throws IOException
     */
    public synchronized void add(final File add, final boolean compress, final String path) throws ZipIOException, IOException {
        if (add == null || !add.exists()) { throw new ZipIOException("add " + add.getAbsolutePath() + " invalid"); }
        if (add.isFile()) {
            addFileInternal(add, compress, path);
        } else if (add.isDirectory()) {
            addDirectoryInternal(add, compress, path);
        } else {
            throw new ZipIOException("add " + add.getAbsolutePath() + " invalid");
        }
    }

    public synchronized void addByteArry(final byte[] data, final boolean compress, final String path, final String name) throws IOException, ZipIOException {
        boolean zipEntryAdded = false;
        try {
            if (data == null) { throw new ZipIOException("data array is invalid"); }
            final ZipEntry zipAdd = new ZipEntry((path != null && path.trim().length() > 0 ? path + "/" : "") + name);
            zipAdd.setSize(data.length);
            if (compress) {
                zipAdd.setMethod(ZipEntry.DEFLATED);
            } else {
                zipAdd.setMethod(ZipEntry.STORED);
                zipAdd.setCompressedSize(data.length);
                /* STORED must have a CRC32! */
                zipAdd.setCrc(Hash.getCRC32(data));
            }
            zipStream.putNextEntry(zipAdd);
            zipEntryAdded = true;
            zipStream.write(data, 0, data.length);
        } finally {
            if (zipEntryAdded) {
                zipStream.closeEntry();
            }
        }
    }

    /**
     * add given Directory to this ZipFile
     * 
     * @param addDirectory
     *            Directory to add
     * @param compress
     *            compress or store
     * @param path
     *            customized path
     * @throws ZipIOException
     * @throws IOException
     */
    public synchronized void addDirectory(final File addDirectory, final boolean compress, final String path) throws ZipIOException, IOException {
        addDirectoryInternal(addDirectory, compress, path);
    }

    private void addDirectoryInternal(final File addDirectory, final boolean compress, final String path) throws ZipIOException, IOException {
        if (addDirectory == null || !addDirectory.isDirectory() || !addDirectory.exists()) { throw new ZipIOException("addDirectory " + addDirectory.getAbsolutePath() + " invalid"); }
        final File[] list = addDirectory.listFiles();
        if (list == null) { return; }
        for (final File add : list) {
            if (add.isFile()) {
                addFileInternal(add, compress, (path != null && path.trim().length() > 0 ? path + "/" : "") + addDirectory.getName());
            } else if (add.isDirectory()) {
                addDirectoryInternal(add, compress, (path != null && path.trim().length() > 0 ? path + "/" : "") + addDirectory.getName());
            } else {
                throw new ZipIOException("addDirectory " + addDirectory.getAbsolutePath() + " invalid");
            }
        }
    }

    /**
     * Add file
     * 
     * @param addFile
     * @param compress
     * @param fullPath
     *            full path incl. filename
     * @throws ZipIOException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void addFile(final File addFile, final boolean compress, final String fullPath) throws ZipIOException, IOException, FileNotFoundException {
        FileInputStream fin = null;
        boolean zipEntryAdded = false;

        try {
            if (addFile == null || !addFile.isFile() || !addFile.exists()) { throw new ZipIOException("addFile " + addFile.getAbsolutePath() + " invalid"); }
            final ZipEntry zipAdd = new ZipEntry(fullPath);
            zipAdd.setSize(addFile.length());
            if (compress) {
                zipAdd.setMethod(ZipEntry.DEFLATED);
            } else {
                zipAdd.setMethod(ZipEntry.STORED);
                zipAdd.setCompressedSize(addFile.length());
                /* STORED must have a CRC32! */
                zipAdd.setCrc(Hash.getCRC32(addFile));
            }
            fin = new FileInputStream(addFile);
            zipStream.putNextEntry(zipAdd);
            zipEntryAdded = true;
            int len;
            while ((len = fin.read(buf)) > 0) {
                zipStream.write(buf, 0, len);
            }
        } finally {
            if (zipEntryAdded) {
                zipStream.closeEntry();
            }
            if (fin != null) {
                fin.close();
            }
        }
    }

    private void addFileInternal(final File addFile, final boolean compress, final String path) throws ZipIOException, IOException {

        final String fullPath = (path != null && path.trim().length() > 0 ? path + "/" : "") + addFile.getName();

        addFile(addFile, compress, fullPath);
    }

    /**
     * add given File to this ZipFile
     * 
     * @param addFile
     *            File to add
     * @param compress
     *            compress or store
     * @param path
     *            customized path without filename!
     * @throws ZipIOException
     * @throws IOException
     */
    public synchronized void addFileToPath(final File addFile, final boolean compress, final String path) throws ZipIOException, IOException {
        addFileInternal(addFile, compress, path);
    }

    /**
     * closes the ZipFile
     * 
     * @throws Throwable
     */
    public synchronized void close() throws IOException {
        Throwable e = null;
        try {
            try {
                zipStream.flush();
            } catch (final Throwable e2) {
                if (e == null) {
                    e = e2;
                }
            }
            try {
                fileStream.flush();
            } catch (final Throwable e2) {
                if (e == null) {
                    e = e2;
                }
            }
            try {
                zipStream.close();
            } catch (final Throwable e2) {
                if (e == null) {
                    e = e2;
                }
            }
            try {
                fileStream.close();
            } catch (final Throwable e2) {
                if (e == null) {
                    e = e2;
                }
            }
        } finally {
            zipStream = null;
            fileStream = null;
        }
        if (e != null) { throw new IOException(e); }
    }

    /**
     * opens the zipFile for further use
     * 
     * @param overwrite
     *            overwrite existing zipFiles?
     * @throws ZipIOException
     * @throws FileNotFoundException
     */
    private void openZip(final boolean overwrite) throws ZipIOException, FileNotFoundException {
        if (fileStream != null && zipStream != null) { return; }
        if (zipFile == null || zipFile.isDirectory()) { throw new ZipIOException("invalid zipFile"); }
        if (zipFile.exists() && !overwrite) { throw new ZipIOException("zipFile already exists"); }

        fileStream = new FileOutputStream(zipFile);
        zipStream = new ZipOutputStream(fileStream);
    }

}
