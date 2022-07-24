package PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LaserMeasurementObjects {

    public LaserMeasurementObjects(RemoteWebDriver driver){
        PageFactory.initElements(driver, this);
    }

    //Laser Peak Power Url
    public String laserMeasurementUrl = "https://www.ophiropt.com/laser--measurement/laser-power-energy-meters/services/peak-power-calculator";

    //Laser - Beam Profile
    @FindBy(css = "#beamprf[value='Tophat']")
    public WebElement beamProfileTophatRadio;
    @FindBy(css = "#beamprf[value='Gaussian']")
    public WebElement beamProfileGaussianRadio;

    //Laser - Beam Shape
    @FindBy(id = "shapeBox")
    public WebElement beamShapeBox;
    @FindBy(id = "beam1Circ")
    public WebElement beamShapeCircularRadio;
    @FindBy(id = "beam1Rect")
    public WebElement beamShapeRectangularRadio;

    //Laser Parameters
    @FindBy(id = "beamDiameter")
    public WebElement parametersDiameterTextBox;

    @FindBy(id = "enerpow1")
    public WebElement parametersMaxPowerRadio;
    @FindBy(id = "max_pwr")
    public WebElement parametersMaxPowerTextBox;
    @FindBy(id = "pwr_unit")
    public WebElement parametersMaxPowerUnitDropdown;
    @FindBy(id = "repRate")
    public WebElement parametersRepetitionRate;
    @FindBy(id = "rep_unit")
    public WebElement parametersRepetitionUnitDropdown;
    @FindBy(id = "pulse")
    public WebElement parametersPulseWidth;
    @FindBy(id = "pulse_unit")
    public WebElement parametersPulseUnit;

    //Calculate button
    @FindBy(id = "btnCalculate")
    public WebElement calculateButton;

    //Results fields
    @FindBy (id = "peakPowerRes")
    public WebElement peakPowerResult;
    @FindBy (id = "peakPowerDenRes")
    public WebElement peakPowerDensityResult;
}
