package lcp_app;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.jgeocoder.AddressComponent;
import net.sourceforge.jgeocoder.us.AddressParser;
import org.apache.commons.io.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class Crawler 
{
    private String lclAddy = null;
    private String lclFax = null;
    private String lclPhone = null;
    private String lclEmail = null;
    private String lclURL = null;
    private String getURL(){return lclURL;}
    private String getAddy(){return lclAddy;}
    private String getFax(){return lclFax;}
    private String getPhone(){return lclPhone;}
    private String getEmail(){return lclEmail;}
    private void setAddy(String remAddy){lclAddy = remAddy;}
    private void setFax(String remFax){lclFax = remFax;}
    private void setPhone(String remPhone){lclPhone = remPhone;}
    private void setEmail(String remEmail){lclEmail = remEmail;}
    private void setURL(String remURL){lclURL = remURL;}
    public List<String[]> xlsAdds = new ArrayList<String[]>();
    
    public Crawler()
    {
        super();
    }
    
    public void goCrawl(File remFile)
    {
        String myPath = remFile.getAbsolutePath().substring(0, remFile.getAbsolutePath().lastIndexOf(File.separator));
        String ext = FilenameUtils.getExtension(remFile.getName());
                
        try 
        {
            if(ext.equalsIgnoreCase("xls"))
            {
                //Create & Copy Data
                FileInputStream fis = new FileInputStream(remFile);
                Workbook oldWB = new HSSFWorkbook(fis);
                File test = new File(myPath + File.separator + "populated.xls");
                if(!test.exists())
                    test.createNewFile();
                FileOutputStream fos = new FileOutputStream(test);
                oldWB.write(fos);
                fis.close();
                fos.close();
                
                //Use New File to Crawl
                FileInputStream newFIS = new FileInputStream(myPath + File.separator + "populated.xls");
                Workbook myWB = new HSSFWorkbook(newFIS);
                Sheet sheet = myWB.getSheet("contact");
                        
                Iterator<Row> rowIterator = sheet.iterator();
                int e = 0;
                while(rowIterator.hasNext())
                {
                    Row row = null;
                    if(e < 1)
                        rowIterator.next();
                    else
                    {
                        row = rowIterator.next();
                        String tmpURL = row.getCell(1).toString();
                        if(!tmpURL.contains("http"))
                            tmpURL += "http://www." + tmpURL;
                        else if(!tmpURL.contains("www"))
                            tmpURL = "http://www." + getDomainName(tmpURL);   
                        
                        char t = tmpURL.charAt(tmpURL.length() - 1);
                        if(t != '/')
                            tmpURL += "/";
                        
                        WebClient webClient = new WebClient();
                        webClient.getOptions().setCssEnabled(false);
                        webClient.getOptions().setJavaScriptEnabled(false);
                        webClient.getOptions().setThrowExceptionOnScriptError(false);
                        webClient.getOptions().setTimeout(10000);
                        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                        webClient.waitForBackgroundJavaScript(30000);
                        
                        String[] potPages = {"/about", "/About", "/ABOUT", "/contact", "/Contact", "/contact-us", "/contactus", "/CONTACT"};
                        outerloop: for(int j = 0; j < potPages.length; j++)
                        {
                            HtmlPage aboutResult = webClient.getPage(tmpURL + potPages[j]);
                            if(aboutResult.isHtmlPage())
                            {
                                String holdMe = aboutResult.asText();
                                this.parseHTML(holdMe, tmpURL);                                
                            }
                        }
                    }
                    e++;
                    
                    //add list
                    if(this.getURL() == null)
                        this.setURL("---");
                    else if(this.getAddy() == null)
                        this.setAddy("---");
                    else if(this.getEmail() == null)
                        this.setEmail("---");
                    else if(this.getFax() == null)
                        this.setFax("---");
                    else if(this.getPhone() == null)
                        this.setPhone("---");                                        
                    String[] holdArr = {this.getURL(), this.getPhone(), this.getFax(), this.getEmail(), this.getAddy()};
                    xlsAdds.add(holdArr);
                    this.setAddy(null);
                    this.setEmail(null);
                    this.setFax(null);
                    this.setPhone(null);
                    this.setURL(null);
                }
            }
            else if(ext.equalsIgnoreCase("xlsx"))
            {
                //Create & Copy Data
                FileInputStream fis = new FileInputStream(remFile);
                Workbook oldWB = new XSSFWorkbook(fis);
                File test = new File(myPath + File.separator + "populated.xlsx");
                if(!test.exists())
                    test.createNewFile();
                FileOutputStream fos = new FileOutputStream(test);
                oldWB.write(fos);
                fis.close();
                fos.close();
                
                //Use New File to Crawl
                FileInputStream newFIS = new FileInputStream(myPath + File.separator + "populated.xlsx");
                Workbook myWB = new XSSFWorkbook(newFIS);
                Sheet sheet = myWB.getSheet("contact");
                        
                Iterator<Row> rowIterator = sheet.iterator();
                int e = 0;
                while(rowIterator.hasNext())
                {
                    Row row = null;
                    if(e < 1)
                        rowIterator.next();
                    else
                    {
                        row = rowIterator.next();
                        String tmpURL = row.getCell(1).toString();
                        if(!tmpURL.contains("http"))
                            tmpURL += "http://www." + tmpURL;
                        else if(!tmpURL.contains("www"))
                            tmpURL = "http://www." + getDomainName(tmpURL);   
                        
                        char t = tmpURL.charAt(tmpURL.length() - 1);
                        if(t != '/')
                            tmpURL += "/";
                        
                        WebClient webClient = new WebClient();
                        webClient.getOptions().setCssEnabled(false);
                        webClient.getOptions().setJavaScriptEnabled(false);
                        webClient.getOptions().setThrowExceptionOnScriptError(false);
                        webClient.getOptions().setTimeout(10000);
                        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                        webClient.waitForBackgroundJavaScript(30000);
             
                        String[] potPages = {"/about", "/About", "/ABOUT", "/contact", "/Contact", "/contact-us", "/CONTACT"};
                        outerloop: for(int j = 0; j < potPages.length; j++)
                        {
                            HtmlPage aboutResult = webClient.getPage(tmpURL + potPages[j]);
                            if(aboutResult.isHtmlPage())
                            {
                                String holdMe = aboutResult.asText();
                                this.parseHTML(holdMe, tmpURL);
                            }
                        }
                    }
                    e++;
                    
                    //add list
                    if(this.getURL() == null)
                        this.setURL("---");
                    else if(this.getAddy() == null)
                        this.setAddy("---");
                    else if(this.getEmail() == null)
                        this.setEmail("---");
                    else if(this.getFax() == null)
                        this.setFax("---");
                    else if(this.getPhone() == null)
                        this.setPhone("---");    
                    String[] holdArr = {this.getURL(), this.getPhone(), this.getFax(), this.getEmail(), this.getAddy()};
                    xlsAdds.add(holdArr);
                    this.setAddy(null);
                    this.setEmail(null);
                    this.setFax(null);
                    this.setPhone(null);
                    this.setURL(null);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }        
        System.exit(0);
    }
     
    public void parseHTML(String remHTML, String remURL)
    {
        if(this.getURL() == null)
        {
            this.setURL(remURL);
            System.out.println("MYURL: " + this.getURL());
        }
        
        Map<AddressComponent, String> parsedAddr  = AddressParser.parseAddress(remHTML);
        if(parsedAddr != null && this.getAddy() == null)
        {
            this.setAddy(parsedAddr.toString());
            System.out.println(this.getAddy());
        }   
        
        String lines[] = remHTML.split("\\r?\\n");
        for(int d = 0; d < lines.length; d++)
        {
            if(lines[d].contains("one") && this.getPhone() == null)
            {
                Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                Matcher matcher = pattern.matcher(lines[d]);
                if(matcher.find())
                {
                        System.out.println("MYPHONE: " + matcher.group(0));
                        this.setPhone(matcher.group(0));
                }
                else
                {
                    Pattern ppattern = Pattern.compile("\\(\\d{3}\\) \\d{3}-\\d{4}");
                    Matcher pmatcher = ppattern.matcher(lines[d]);
                    if(pmatcher.find())
                    {
                            System.out.println("MYPHONE: " + pmatcher.group(0));
                            this.setPhone(pmatcher.group(0));
                    }
                    else
                    {
                        Pattern pspattern = Pattern.compile("(\\d{3})\\d{3}-\\d{4}");
                        Matcher psmatcher = pspattern.matcher(lines[d]);
                        if(psmatcher.find())
                        {
                            {
                                System.out.println("MYPHONE: " + psmatcher.group(0));
                                this.setPhone(psmatcher.group(0));
                            }
                        }
                    }
                }
            }
            else if(lines[d].contains("(p)") && this.getPhone() == null)
            {
                Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                Matcher matcher = pattern.matcher(lines[d]);
                if(matcher.find())
                {
                        System.out.println("MYPHONE: " + matcher.group(0));
                        this.setPhone(matcher.group(0));
                }
                else
                {
                    Pattern ppattern = Pattern.compile("\\(\\d{3}\\) \\d{3}-\\d{4}");
                    Matcher pmatcher = ppattern.matcher(lines[d]);
                    if(pmatcher.find())
                    {
                            System.out.println("MYPHONE: " + pmatcher.group(0));
                            this.setPhone(pmatcher.group(0));
                    }
                    else
                    {
                        Pattern pspattern = Pattern.compile("(\\d{3})\\d{3}-\\d{4}");
                        Matcher psmatcher = pspattern.matcher(lines[d]);
                        if(psmatcher.find())
                        {
                            {
                                System.out.println("MYPHONE: " + psmatcher.group(0));
                                this.setPhone(psmatcher.group(0));
                            }
                        }
                    }
                }
            }
            else if(lines[d].contains("p:") && this.getPhone() == null)
            {
                Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                Matcher matcher = pattern.matcher(lines[d]);
                if(matcher.find())
                {
                        System.out.println("MYPHONE: " + matcher.group(0));
                        this.setPhone(matcher.group(0));
                }
                else
                {
                    Pattern ppattern = Pattern.compile("\\(\\d{3}\\) \\d{3}-\\d{4}");
                    Matcher pmatcher = ppattern.matcher(lines[d]);
                    if(pmatcher.find())
                    {
                            System.out.println("MYPHONE: " + pmatcher.group(0));
                            this.setPhone(pmatcher.group(0));
                    }
                    else
                    {
                        Pattern pspattern = Pattern.compile("(\\d{3})\\d{3}-\\d{4}");
                        Matcher psmatcher = pspattern.matcher(lines[d]);
                        if(psmatcher.find())
                        {
                            {
                                System.out.println("MYPHONE: " + psmatcher.group(0));
                                this.setPhone(psmatcher.group(0));
                            }
                        }
                    }
                }
            }
            else if(lines[d].contains("ax") && this.getFax() == null)
            {
                Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                Matcher matcher = pattern.matcher(lines[d]);
                if(matcher.find())
                {
                        System.out.println("MYFAX: " + matcher.group(0));
                        this.setFax(matcher.group(0));
                }
                else
                {
                    Pattern ppattern = Pattern.compile("\\(\\d{3}\\) \\d{3}-\\d{4}");
                    Matcher pmatcher = ppattern.matcher(lines[d]);
                    if(pmatcher.find())
                    {
                            System.out.println("MYFAX: " + pmatcher.group(0));
                            this.setFax(pmatcher.group(0));
                    }
                    else
                    {
                        Pattern pspattern = Pattern.compile("(\\d{3})\\d{3}-\\d{4}");
                        Matcher psmatcher = pspattern.matcher(lines[d]);
                        if(psmatcher.find())
                        {
                                System.out.println("MYFAX: " + psmatcher.group(0));
                                this.setFax(psmatcher.group(0));
                        }
                    }
                }
            }
            else if(lines[d].contains("(f)") && this.getFax() == null)
            {
                Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                Matcher matcher = pattern.matcher(lines[d]);
                if(matcher.find())
                {
                        System.out.println("MYFAX: " + matcher.group(0));
                        this.setFax(matcher.group(0));
                }
                else
                {
                    Pattern ppattern = Pattern.compile("\\(\\d{3}\\) \\d{3}-\\d{4}");
                    Matcher pmatcher = ppattern.matcher(lines[d]);
                    if(pmatcher.find())
                    {
                            System.out.println("MYFAX: " + pmatcher.group(0));
                            this.setFax(pmatcher.group(0));
                    }
                    else
                    {
                        Pattern pspattern = Pattern.compile("(\\d{3})\\d{3}-\\d{4}");
                        Matcher psmatcher = pspattern.matcher(lines[d]);
                        if(psmatcher.find())
                        {
                                System.out.println("MYFAX: " + psmatcher.group(0));
                                this.setFax(psmatcher.group(0));
                        }
                    }
                }
            }
            else if(lines[d].contains("f:") && this.getFax() == null)
            {
                Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                Matcher matcher = pattern.matcher(lines[d]);
                if(matcher.find())
                {
                        System.out.println("MYFAX: " + matcher.group(0));
                        this.setFax(matcher.group(0));
                }
                else
                {
                    Pattern ppattern = Pattern.compile("\\(\\d{3}\\) \\d{3}-\\d{4}");
                    Matcher pmatcher = ppattern.matcher(lines[d]);
                    if(pmatcher.find())
                    {
                            System.out.println("MYFAX: " + pmatcher.group(0));
                            this.setFax(pmatcher.group(0));
                    }
                    else
                    {
                        Pattern pspattern = Pattern.compile("(\\d{3})\\d{3}-\\d{4}");
                        Matcher psmatcher = pspattern.matcher(lines[d]);
                        if(psmatcher.find())
                        {
                                System.out.println("MYFAX: " + psmatcher.group(0));
                                this.setFax(psmatcher.group(0));
                        }
                    }
                }
            }
            else if(lines[d].contains("ail") && this.getEmail() == null)
            {
                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(lines[d]);
                if(m.find()) 
                {
                    this.setEmail(m.group(0));
                    System.out.println("MYEMAIL: " + m.group(0)); 
                }
            }
            else if(lines[d].contains("@") && this.getEmail() == null)
            {
                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(lines[d]);
                if(m.find()) 
                {
                    this.setEmail(m.group(0));
                    System.out.println("MYEMAIL: " + m.group(0)); 
                }
            }
            else    //zip code
            {
                if(this.getAddy() == null)
                {
                    Pattern zippattern = Pattern.compile("(\\d{5})");
                    Matcher zipmatcher = zippattern.matcher(lines[d]);
                    if(zipmatcher.find())
                    {
                        String myHold = this.shortAddy(lines[d-2]) + " " + this.shortAddy(lines[d-1]) + " " + this.shortAddy(lines[d]);
                        System.out.println("MYADDRESS: " + this.stripAddy(myHold));
                        this.setAddy(this.stripAddy(myHold));
                    }
                }                
            }
        }
    }
    
    public String shortAddy(String remData)
    {
        if(remData.length() > 150)
            return remData.substring(remData.length()-15, remData.length()-1);
        else 
            return remData;
    }
  
    public String stripAddy(String rawData)
    {
        String retString = "";
        StringTokenizer st = new StringTokenizer(rawData);
        while(st.hasMoreElements())
        {
            String hold  = st.nextToken();
            hold = hold.replaceAll("[^\\p{ASCII}]", "");
            
            Pattern psapattern = Pattern.compile("\\d{3}-\\d{4}");
            Matcher psamatcher = psapattern.matcher(hold);
            
            Pattern pspattern = Pattern.compile("(\\d{3})\\d{3}-\\d{4}");
            Matcher psmatcher = pspattern.matcher(hold);
            
            Pattern areapattern = Pattern.compile("\\(\\d{3}\\)");
            Matcher areamatcher = areapattern.matcher(hold);
            
            if(areamatcher.find())
                 ;
            else if(psmatcher.find())
                ;
            else if(psamatcher.find())
                ;
            else if(hold.equalsIgnoreCase("home"))
                ;
            else if(hold.equalsIgnoreCase("main"))
                ;
            else if(hold.equalsIgnoreCase("(main"))
                ;
            else if(hold.equalsIgnoreCase("office"))
                ;
            else if(hold.equalsIgnoreCase("office)"))
                ;
            else if(hold.equalsIgnoreCase("toll"))
                ;
            else if(hold.equalsIgnoreCase("free:"))
                ;
            else if(hold.equalsIgnoreCase("fax:"))
                ;
            else if(hold.equalsIgnoreCase("phone"))
                ;
            else if(hold.equalsIgnoreCase("phone:"))
                ;
            else if(hold.equalsIgnoreCase("appointment"))
                ;
            else if(hold.equalsIgnoreCase("contact"))
                ;
            else if(hold.equalsIgnoreCase("email"))
                ;
            else if(hold.equalsIgnoreCase("appearances"))
                ;
            else if(hold.equalsIgnoreCase("thought"))
                ;
            else if(hold.equalsIgnoreCase("leadership"))
                ;
            else if(hold.equalsIgnoreCase("email:"))
                ;
            else if(hold.equalsIgnoreCase("get"))
                ;
            else if(hold.equalsIgnoreCase("with"))
                ;
            else if(hold.equalsIgnoreCase("(t)"))
                ;
            else if(hold.equalsIgnoreCase("(f)"))
                ; 
            else if(hold.equalsIgnoreCase("f:"))
                ; 
            else if(hold.equalsIgnoreCase("t:"))
                ; 
            else if(hold.contains("@"))
                ;
            else if(hold.contains("+"))
                ;
            else if(hold.contains("about"))
                ;
            else
                retString += hold + " ";
        }
        return retString;
    }
    
    public String getDomainName(String url) throws URISyntaxException
    {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}