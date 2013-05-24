import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.br.weatherconsult.MainActivity;
import com.br.weatherconsult.ReadXMLFile;
import com.br.weatherconsult.ReadXMLFile.MetarInfo;


public class MainTest {
	
	@Test
	public void test() {
		//ReadXMLFile readXML = new ReadXMLFile();
		//MetarInfo info = readXML.getMetarInfo();
		long result =  10 * 5;
		assertEquals("10 x 5 must be 50",50, result);
		
	}

}
