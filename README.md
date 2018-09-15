# image2Tif
图片转TIF格式工具类及需要的3个jar包：jai_core-1.1.3.jar、jai_imageio.jar、jai-codec-1.1.3.jar

工具类介绍：
传入文件的绝对路径，返回的是一个TIF格式dpi为300的图片路径，dpi可以自己设置值。
其中有遇到图片位深度为8位的图片无法转换，所以多加了一个filterFilePath，用于过滤一次，统一转换成位深度为24的JPG图片，然后再进行TIF编码。
