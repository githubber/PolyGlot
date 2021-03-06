/*
 * Copyright (c) 2014, Draque Thompson, draquemail@gmail.com
 * All rights reserved.
 *
 * Licensed under: Creative Commons Attribution-NonCommercial 4.0 International Public License
 * See LICENSE.TXT included with this code to read the full license agreement.
 *
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

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author draque
 */
public class PropertiesManager {
    private Font font = null;
    private Integer fontStyle = 0;
    private Integer fontSize = 0;
    private boolean proAutoPop = false;
    private final Map alphaOrder;
    private String alphaPlainText = "";
    private String langName = "";
    private boolean typesMandatory = false;
    private boolean localMandatory = false;
    private boolean wordUniqueness = false;
    private boolean localUniqueness = false;
    private boolean ignoreCase = false;
    private boolean disableProcRegex = false;
    String fontName = "";
    
    public PropertiesManager() {
        alphaOrder = new HashMap<Character, Integer>();
    }
    
    public void setDisableProcRegex(boolean _disableProcRegex) {
        disableProcRegex = _disableProcRegex;
    }
    
    public boolean isDisableProcRegex() {
        return disableProcRegex;
    }
    
    /**
     * Sets ignore case value for dictionary
     * @param _ignoreCase new value
     */
    public void setIgnoreCase(boolean _ignoreCase) {
        ignoreCase = _ignoreCase;
    }
    
    /**
     * Retrieves ignore case 
     * @return ignore case status of dictionary
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }
    
    /**
     * gets font name for table keeping loading purposes. Does NOT populate from actual font
     * @return font name if any
     */
    public String getFontName() {
        return fontName;
    }
    
    /**
     * Sets font name for table keeping loading purposes. Does NOT populate from actual font
     * @param _fontName name to set
     */
    public void setFontName(String _fontName) {
        fontName = _fontName;
    }
    
    /**
     * @return the fontCon
     */
    public Font getFontCon() {
        return font == null? null : font.deriveFont(fontStyle, fontSize);
    }

    /**
     * @param fontCon the fontCon to set
     */
    public void setFontCon(Font fontCon) {
        font = fontCon;
    }

    /**
     * @return the fontStyle
     */
    public Integer getFontStyle() {
        return fontStyle;
    }

    /**
     * @param fontStyle the fontStyle to set
     */
    public void setFontStyle(Integer fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
     * @return the fontSize
     */
    public Integer getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the proAutoPop
     */
    public boolean isProAutoPop() {
        return proAutoPop;
    }

    /**
     * @param proAutoPop the proAutoPop to set
     */
    public void setProAutoPop(boolean proAutoPop) {
        this.proAutoPop = proAutoPop;
    }

    /**
     * @return the alphaOrder
     */
    public Map getAlphaOrder() {
        return alphaOrder;
    }

    /**
     * @param order alphabetical order
     */
    public void setAlphaOrder(String order) {
        alphaPlainText = order;
        
        alphaOrder.clear();
        
        for (int i = 0; i < order.length(); i++) {
            alphaOrder.put(order.charAt(i), i);
        }
    }

    /**
     * @return the alphaPlainText
     */
    public String getAlphaPlainText() {
        return alphaPlainText;
    }

    /**
     * @return the langName
     */
    public String getLangName() {
        return langName;
    }

    /**
     * @param langName the langName to set
     */
    public void setLangName(String langName) {
        this.langName = langName;
    }

    /**
     * @return the typesMandatory
     */
    public boolean isTypesMandatory() {
        return typesMandatory;
    }

    /**
     * @param typesMandatory the typesMandatory to set
     */
    public void setTypesMandatory(boolean typesMandatory) {
        this.typesMandatory = typesMandatory;
    }

    /**
     * @return the localMandatory
     */
    public boolean isLocalMandatory() {
        return localMandatory;
    }

    /**
     * @param localMandatory the localMandatory to set
     */
    public void setLocalMandatory(boolean localMandatory) {
        this.localMandatory = localMandatory;
    }

    /**
     * @return the wordUniqueness
     */
    public boolean isWordUniqueness() {
        return wordUniqueness;
    }

    /**
     * @param wordUniqueness the wordUniqueness to set
     */
    public void setWordUniqueness(boolean wordUniqueness) {
        this.wordUniqueness = wordUniqueness;
    }

    /**
     * @return the localUniqueness
     */
    public boolean isLocalUniqueness() {
        return localUniqueness;
    }

    /**
     * @param localUniqueness the localUniqueness to set
     */
    public void setLocalUniqueness(boolean localUniqueness) {
        this.localUniqueness = localUniqueness;
    }

    public String buildPropertiesReport() {
        String ret = "";
        
        ret += "Language Name: " + langName + "<br><br>";
        
        return ret;
    }
    
    /**
     * Writes all dictionary properties to XML document
     * @param doc Document to write dictionary properties to
     * @param rootElement root element of document
     */
    public void writeXML(Document doc, Element rootElement) {
        Element wordValue;
        
        // store font for Conlang words
        wordValue = doc.createElement(XMLIDs.fontConXID);
        Font curFont = getFontCon();
        wordValue.appendChild(doc.createTextNode(curFont == null ? "" : curFont.getName()));
        rootElement.appendChild(wordValue);

        // store font style
        wordValue = doc.createElement(XMLIDs.langPropFontStyleXID);
        wordValue.appendChild(doc.createTextNode(getFontStyle().toString()));
        rootElement.appendChild(wordValue);

        // store font for Local words
        wordValue = doc.createElement(XMLIDs.langPropFontSizeXID);
        wordValue.appendChild(doc.createTextNode(getFontSize().toString()));
        rootElement.appendChild(wordValue);

        // store name for conlang
        wordValue = doc.createElement(XMLIDs.langPropLangNameXID);
        wordValue.appendChild(doc.createTextNode(getLangName()));
        rootElement.appendChild(wordValue);

        // store alpha order for conlang
        wordValue = doc.createElement(XMLIDs.langPropAlphaOrderXID);
        wordValue.appendChild(doc.createTextNode(getAlphaPlainText()));
        rootElement.appendChild(wordValue);

        // store option to autopopulate pronunciations
        wordValue = doc.createElement(XMLIDs.proAutoPopXID);
        wordValue.appendChild(doc.createTextNode(isProAutoPop() ? "T" : "F"));
        rootElement.appendChild(wordValue);

        // store option for mandatory Types
        wordValue = doc.createElement(XMLIDs.langPropTypeMandatoryXID);
        wordValue.appendChild(doc.createTextNode(isTypesMandatory() ? "T" : "F"));
        rootElement.appendChild(wordValue);

        // store option for mandatory Local word
        wordValue = doc.createElement(XMLIDs.langPropLocalMandatoryXID);
        wordValue.appendChild(doc.createTextNode(isLocalMandatory() ? "T" : "F"));
        rootElement.appendChild(wordValue);

        // store option for unique local word
        wordValue = doc.createElement(XMLIDs.langPropLocalUniquenessXID);
        wordValue.appendChild(doc.createTextNode(isLocalUniqueness() ? "T" : "F"));
        rootElement.appendChild(wordValue);

        // store option for unique conwords
        wordValue = doc.createElement(XMLIDs.langPropWordUniquenessXID);
        wordValue.appendChild(doc.createTextNode(isWordUniqueness() ? "T" : "F"));
        rootElement.appendChild(wordValue);
        
        // store option for ignoring case
        wordValue = doc.createElement(XMLIDs.langPropIgnoreCase);
        wordValue.appendChild(doc.createTextNode(isIgnoreCase() ? "T" : "F"));
        rootElement.appendChild(wordValue);
        
        // store option for disabling regex or pronunciations
        wordValue = doc.createElement(XMLIDs.langPropDisableProcRegex);
        wordValue.appendChild(doc.createTextNode(isDisableProcRegex()? "T" : "F"));
        rootElement.appendChild(wordValue);
    }
}
