�����У������ǵ�����һ������String[] header = {"�µ�����","������","ȡ��ԭ��","���ͷ�ʽ","֧����ʽ","�ͻ���ע"};
���ݣ������Ƕ������ļ��� List<List<String>> orderList  = new ArrayList<String>();
�������ݣ�List<String> orderArray = new ArrayList<>(7);// 7 Ϊ��ʼ����
	orderList.add(orderArray);
����excel�ļ�����·����url

// ��һ��������һ��webbook����Ӧһ��Excel�ļ�
            HSSFWorkbook wb = new HSSFWorkbook();
            // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
            HSSFSheet sheet = wb.createSheet("Sheet1");
            // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
            HSSFRow row = sheet.createRow((int) 0);
            // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ
            for (short i = 0; i < header.length; i++) {
                sheet.setColumnWidth(i, 10*512);//�����п�
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(header[i]);
                cell.setCellValue(text);
            }
            //���岽��д��ʵ������
            for (int i = 0; i <orderList.size(); i++)
            {
                HSSFRow row1 = sheet.createRow(i+1);
                //������Ԫ����ֵ
                List<String> list = orderList.get(i);
                String[] listArr = list.toArray(new String[list.size()]);//ָ������ת������ֹ���쳣
                for(int j = 0;j < listArr.length;j++){
                    HSSFCell cell = row1.createCell(j);
                    cell.setCellStyle(style);
                    HSSFRichTextString text = new HSSFRichTextString(listArr[j]);
                    cell.setCellValue(text);
                }
            }

            File file = new File(url);
            String excelurl = "";//·��
            String times = DateUtils.getDateStr(DateUtils.DEFAULT_FORMAT);//����ʱ��
            //����ļ��в������򴴽�
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            FileOutputStream fout = new FileOutputStream(url + "������ϸ��_" + times + ".xls");
            wb.write(fout);
            excelurl = times + ".xls";
            fout.close();
            wb.close();
            filePath = url + excelurl;






			