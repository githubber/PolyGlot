/*
 * Copyright (c) 2014, Draque
 * All rights reserved.
 *
 * Licensed under: Creative Commons Attribution-NonCommercial 4.0 International Public License
 *  See LICENSE.TXT included with this code to read the full license agreement.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package PolyGlot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

/**
 * This class exports an existing dictionary to an excel spreadsheet
 *
 * @author Draque
 */
public class ExcelExport {
    
    /**
     * Exports a dictionary to an excel file (externally facing)
     * @param fileName Filename to export to
     * @param core dictionary core
     * @throws Exception on write error
     */
    public static void exportExcelDict(String fileName, DictCore core) throws Exception {
        ExcelExport e = new ExcelExport();
        
        e.export(fileName, core);
    }
    
    private Object[] getWordForm(ConWord conWord, DictCore core) {
        List<String> ret = new ArrayList<String>();
        String declensionCell = "";
        
        ret.add(conWord.getValue());
        ret.add(conWord.getLocalWord());
        ret.add(conWord.getWordType());
        ret.add(conWord.getPronunciation());
        ret.add(conWord.getGender());
        
        List<DeclensionNode> declensions = core.getDeclensionManager().getDeclensionListWord(conWord.getId());
        
        for(DeclensionNode curNode : declensions) {
            declensionCell += curNode.getNotes() + " : " + curNode.getValue() + "\n";
        }
        
        ret.add(declensionCell);
        ret.add(conWord.getDefinition());        
        
        return ret.toArray();
    }
    
    
    
    /**
     * Exports a dictionary to an excel file
     * @param fileName Filename to export to
     * @param core dictionary core
     * @throws Exception on write error
     */
    private void export(String fileName, DictCore core) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet;
        CellStyle localStyle = workbook.createCellStyle();
        CellStyle conStyle = workbook.createCellStyle();
        Font conFont = workbook.createFont();
        conFont.setFontName(core.getLangFont().getFontName());
        
        localStyle.setWrapText(true);
        
        conStyle.setWrapText(true);
        conStyle.setFont(conFont);
        
        // record words on sheet 1        
        sheet = workbook.createSheet("Lexicon");
        Iterator<ConWord> wordIt = core.getWordIterator();
        
        Row row  = sheet.createRow(0);
        row.createCell(0).setCellValue("CON WORD");
        row.createCell(1).setCellValue("LOCAL WORD");
        row.createCell(2).setCellValue("TYPE");
        row.createCell(3).setCellValue("PRONUNCIATION");
        row.createCell(4).setCellValue("GENDER");
        row.createCell(5).setCellValue("DECLENSIONS");
        row.createCell(6).setCellValue("DEFINITIONS");
        
        for (Integer i = 1; wordIt.hasNext(); i++) {
            Object[] wordArray = getWordForm(wordIt.next(), core);
            row = sheet.createRow(i);
            for (Integer j = 0; j < wordArray.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue((String)wordArray[j]);
                
                if (j == 0) {
                    cell.setCellStyle(conStyle);
                } else {
                    cell.setCellStyle(localStyle);
                }
            }
        }
        
        // record types on sheet 2
        sheet = workbook.createSheet("Types");
        Iterator<TypeNode> typeIt = core.getTypes().getNodeIterator();
        
        row = sheet.createRow(0);
        row.createCell(0).setCellValue("TYPE");
        row.createCell(1).setCellValue("NOTES");
        
        for (Integer i = 1; typeIt.hasNext(); i++) {
            TypeNode curNode = typeIt.next();
            row = sheet.createRow(i);
            
            Cell cell = row.createCell(0);
            cell.setCellValue(curNode.getValue());
            cell = row.createCell(1);
            cell.setCellValue(curNode.getNotes());
            cell.setCellStyle(localStyle);
        }
        
        // record genders on sheet 3
        sheet = workbook.createSheet("Genders");
        Iterator<GenderNode> genderIt = core.getGenders().getNodeIterator();
        
        row = sheet.createRow(0);
        row.createCell(0).setCellValue("GENDER");
        row.createCell(1).setCellValue("NOTES");
        
        for (Integer i = 1; genderIt.hasNext(); i++) {
            GenderNode curNode = genderIt.next();
            row = sheet.createRow(i);
            
            Cell cell = row.createCell(0);
            cell.setCellValue(curNode.getValue());
            cell = row.createCell(1);
            cell.setCellValue(curNode.getNotes());
            cell.setCellStyle(localStyle);
        }
        
        // record pronunciations on sheet 4
        sheet = workbook.createSheet("Pronunciations");
        Iterator<PronunciationNode> procIt = core.getPronunciations();
        
        row = sheet.createRow(0);
        row.createCell(0).setCellValue("CHARACTER(S)");
        row.createCell(1).setCellValue("PRONUNCIATION");
        
        for (Integer i = 1; procIt.hasNext(); i++) {
            PronunciationNode curNode = procIt.next();
            row = sheet.createRow(i);
            
            Cell cell = row.createCell(0);
            cell.setCellStyle(conStyle);
            cell.setCellValue(curNode.getValue());
            
            cell = row.createCell(1);
            cell.setCellStyle(localStyle);
            cell.setCellValue(curNode.getPronunciation());
        }

        try {
            FileOutputStream out
                    = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            throw new Exception("Unable to write file: " + fileName);
        }
    }
}
