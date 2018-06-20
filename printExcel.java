标题行：可以是单独的一个数组String[] header = {"下单日期","订单号","取消原因","配送方式","支付方式","客户备注"};
内容：可以是多个数组的集合 List<List<String>> orderList  = new ArrayList<String>();
单行数据：List<String> orderArray = new ArrayList<>(7);// 7 为初始长度
	orderList.add(orderArray);
设置excel文件导出路径：url

// 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("Sheet1");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            for (short i = 0; i < header.length; i++) {
                sheet.setColumnWidth(i, 10*512);//设置列宽
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(header[i]);
                cell.setCellValue(text);
            }
            //第五步，写入实体数据
            for (int i = 0; i <orderList.size(); i++)
            {
                HSSFRow row1 = sheet.createRow(i+1);
                //创建单元格设值
                List<String> list = orderList.get(i);
                String[] listArr = list.toArray(new String[list.size()]);//指定类型转换，防止跑异常
                for(int j = 0;j < listArr.length;j++){
                    HSSFCell cell = row1.createCell(j);
                    cell.setCellStyle(style);
                    HSSFRichTextString text = new HSSFRichTextString(listArr[j]);
                    cell.setCellValue(text);
                }
            }

            File file = new File(url);
            String excelurl = "";//路径
            String times = DateUtils.getDateStr(DateUtils.DEFAULT_FORMAT);//定义时间
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            FileOutputStream fout = new FileOutputStream(url + "订单明细表_" + times + ".xls");
            wb.write(fout);
            excelurl = times + ".xls";
            fout.close();
            wb.close();
            filePath = url + excelurl;






			