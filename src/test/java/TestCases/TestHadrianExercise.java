package TestCases;

import Base.APIException;
import Base.TestMethods;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;



public class TestHadrianExercise extends TestMethods {

    private final String laserPeakPowerCalcURL = "https://www.ophiropt.com/laser--measurement/laser-power-energy-meters/services/peak-power-calculator";
    private final String trid = "2";

    @Parameters({"environment"})
    @BeforeMethod()
    public void beforeEachTest(@Optional("https://www.ophiropt.com/laser--measurement/laser-power-energy-meters/services/peak-power-calculator") String environment) {

        driver.get(laserPeakPowerCalcURL);
    }

    @Test
    public void testEndToEnd() throws APIException, IOException {
        test = report.createTest("Test Hadrian Exercise End to End");

        verifyURL(laserPeakPowerCalcURL, trid, "1");

        elementSelected(lmo.beamProfileTophatRadio, "Beam Profile: Tophat radio button", trid, "2");
        elementIsNotSelected(lmo.beamProfileGaussianRadio, "Beam Profile: Gaussian radio button", trid, "2");

        elementSelected(lmo.beamShapeCircularRadio, "Beam Shape: Circular radio button", trid, "3");
        elementIsNotSelected(lmo.beamShapeRectangularRadio, "Beam Shape: Rectangular radio button", trid, "3");

        sendTextToInputField(lmo.parametersDiameterTextBox,"1.5", "Diameter textbox", trid, "4");

        clickElement(lmo.parametersMaxPowerRadio, "Max Power (avg) radio button", trid, "5");
        sendTextToInputField(lmo.parametersMaxPowerTextBox,"5", "Max Power (avg) textbox", trid, "5");
        selectElementFromDropdown(lmo.parametersMaxPowerUnitDropdown, "W", "Max Power (avg) unit dropdown", trid, "5");

        sendTextToInputField(lmo.parametersRepetitionRate,"100", "Repetition Rate textbox", trid, "6");
        selectElementFromDropdown(lmo.parametersRepetitionUnitDropdown, "kHz", "Repetition Rate unit dropdown", trid, "6");

        sendTextToInputField(lmo.parametersPulseWidth,"250", "Pulse Width textbox", trid, "7");
        selectElementFromDropdown(lmo.parametersPulseUnit, "ps", "Pulse Width unit dropdown", trid, "7");

        clickElement(lmo.calculateButton, "Calculate button", trid, "8");

        getAttributeFromElement(lmo.peakPowerResult, "value", "200000000.00", "Peak Power Results textbox", trid, "9");
        getAttributeFromElement(lmo.peakPowerResult, "value", "11317684842.09", "Peak Power Density Results textbox", trid, "9");

        clickElement(lmo.beamProfileGaussianRadio, "Beam Profile: Gaussian radio button", trid, "10");
        elementSelected(lmo.beamProfileGaussianRadio, "Beam Profile: Gaussian radio button", trid, "10");
        elementIsNotSelected(lmo.beamProfileTophatRadio, "Beam Profile: Tophat radio button", trid, "10");

        elementIsNotDisplayed(lmo.beamShapeBox, "Beam Shape selection box", trid, "11");

        clickElement(lmo.calculateButton, "Calculate button", trid, "12");
        getAttributeFromElement(lmo.peakPowerResult, "value", "200000000.00", "Peak Power Results textbox", trid, "12");
        getAttributeFromElement(lmo.peakPowerResult, "value", "22635369684.18", "Peak Power Density Results textbox", trid, "12");

    }


}
