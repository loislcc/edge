package edu.buaa.web.rest.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//import android.graphics.Bitmap;

public final class utils {

    /**
     * 字符流写入字符串到txt
     */
    @SuppressWarnings("resource")
    public static void FileString(String path, String data) {
        try {
            FileWriter writer = new FileWriter(path);// 字符流
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 字节输出到txt
     *
     * @param path
     * @param data
     */
    @SuppressWarnings("resource")
    public static void FileString2(String path, String data) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);// 字节流
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 设置编码格式写出到txt
     *
     * @param path
     * @param data
     */
    public static void FileString3(String path, String data) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");// 设置编码格式
            writer.write(data);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 追加写入到txt
     *
     * @param path
     * @param data
     */
    @SuppressWarnings("resource")
    public static void FileString4(String path, String data) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path, true);// 追加写入
            outputStream.write(("\r\n" + data).getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 存储list到文件
     *
     * @param path
     * @param list
     */
    @SuppressWarnings("resource")
    public static <T> void FileWriteListneed(String path, List<T> list) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(list);
            stream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 设置编码格式存储list到txt
     *
     * @param path
     * @param list
     */

    @SuppressWarnings("resource")
    public static <T> void FileWriteList(String path, List<T> list) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            for (T s : list) {
                bufferedWriter.write(s.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    /**
//     * bitmap 写入到本地
//     *
//     * @param path
//     * @param bitmap
//     */
//    @SuppressWarnings("resource")
//    public static void FileBitmap(String path, Bitmap bitmap) {
//        try {
//            FileOutputStream outputStream = new FileOutputStream(path);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    /**
     * 读取本地文件数据设置指定编码
     *
     * @param path
     */
    @SuppressWarnings("resource")
    public static String FileInputString(String path) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String data = null;
            while ((data = reader.readLine()) != null) {
                buffer.append(data + "\r\n");
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 根据字节读取文件
     *
     * @param path
     * @return
     */
    @SuppressWarnings("resource")
    public static String FileInputString2(String path) {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream inputStream = new FileInputStream(path);
            byte[] bytes = new byte[1024];
            int bytead = 0;
            while ((bytead = inputStream.read(bytes)) != -1) {
                buffer.append(new String(bytes, 0, bytead));
            }
            inputStream.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 获取本地文件中的list
     *
     * @param path
     */
//
//    @SuppressWarnings("resource")
//    public static <T> List<Loginfo> FileInputListneed(String path) {
//        List<Loginfo> list = new ArrayList<>();
//        try {
//            FileInputStream inputStream = new FileInputStream(path);
//            ObjectInputStream stream = new ObjectInputStream(inputStream);
//            list = (List<Loginfo>) stream.readObject();
//            inputStream.close();
//            stream.close();
//
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (StreamCorruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return list;
//    }

    /**
     * 高效读取指定编码格式的文件
     * @param path
     * @return
     */
    @SuppressWarnings("resource")
    public static String FileInput3(String path) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String data = null;
            while ((data = bufferedReader.readLine()) != null) {
                buffer.append(data+"\r\n");
            }
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer.toString();
    }



//    public static MultipartFile fileToMulti(File f) throws IOException {
//        InputStream inputStream = new FileInputStream(f);
//        MultipartFile multipartFile = new MockMultipartFile(f.getName(), inputStream);
//        return multipartFile;
//    }

    public static MultipartFile getMulFile(File file) {
        FileItem fileItem = createFileItem(file);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }

    public static FileItem createFileItem(File file)
    {
        String filePath = file.getPath();
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "multipart/form-data", true,
            file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return item;
    }
}
