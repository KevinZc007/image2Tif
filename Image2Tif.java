public class Image2Tif{

    /**
     *
     * 功能描述: 图片转tif格式
     *
     * @param: [fileAbsolutePath]
     * @return: java.lang.String
     * @auther: KevinZc
     * @date: 2018/9/8 22:14
     */
    public String image2Tif(String fileAbsolutePath){
        OutputStream outputStream = null;
        String filterFilePath = null;
        String tifFilePath = null;
        ImageOutputStream ios = null;
        try {
            // 解决位深度太小 start ====注意：8位深度的图片会出现文件损坏问题
            File picture = new File(fileAbsolutePath);
            // 统一进行一次过滤 转换成24位深度
            filterFilePath = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("."))+".JPG";
            tifFilePath = filterFilePath.substring(0, filterFilePath.lastIndexOf("."))+".tif";
            ios = ImageIO.createImageOutputStream(new File(filterFilePath));
            ImageIO.write(ImageIO.read(picture),"JPG", ios);
            // 解决位深度太小 end
            FileSeekableStream stream = new FileSeekableStream(filterFilePath);
            PlanarImage in = JAI.create("stream", stream);
            OutputStream os = null;
            os = new FileOutputStream(tifFilePath);
            // 设置dpi为300
            TIFFEncodeParam param = new TIFFEncodeParam();
            param.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
            TIFFField[] extras = new TIFFField[2];
            extras[0] = new TIFFField(282, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) 300, 1}, {0, 0}});
//            extras[0] = new TIFFField(282, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) dpi, 1}, {0, 0}});
            extras[1] = new TIFFField(283, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) 300, 1}, {0, 0}});
            param.setExtraFields(extras);
            TIFFImageEncoder enc = new TIFFImageEncoder(os, param);
            try {
                enc.encode(in);
                os.flush();
                os.close();
                stream.close();
            } catch (Exception e) {
                logger.error("{}",e );
                throw new RuntimeException(e);
            }
            return tifFilePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}