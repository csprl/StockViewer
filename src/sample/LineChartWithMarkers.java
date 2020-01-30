package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.w3c.dom.css.Rect;
import Main.PearsonCorrelation;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/*
* THIS CLASS WAS COPIED FROM : https://stackoverflow.com/questions/28952133/how-to-add-two-vertical-lines-with-javafx-linechart
* This class allows the linechart to display both vertical and horizontal lines.
* The class has been modified to also display infoboxes and rectangles
* */

public class LineChartWithMarkers<X,Y> extends LineChart {

    public ObservableList<Data<X, Y>> horizontalMarkers;
    public ObservableList<Data<X, Y>> verticalMarkers;
    public ObservableList<Data<X, Y>> verticalZoomMarkers;
    public ObservableList<Data <X, X>> rectangleMarkers;

    public ObservableList<Data<X, Y>> infoBoxes;

    public ObservableList<Data<X, Y>> stackPanes;

    public LineChartWithMarkers(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        super(xAxis, yAxis);

        horizontalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.YValueProperty()});
        horizontalMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());

        verticalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
        verticalMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());

        verticalZoomMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
        verticalZoomMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());

        rectangleMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
        rectangleMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());

        infoBoxes = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
        infoBoxes.addListener((InvalidationListener) observable -> layoutPlotChildren());

        stackPanes = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
        stackPanes.addListener((InvalidationListener) observable -> layoutPlotChildren());

    }

    public void addHorizontalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (horizontalMarkers.contains(marker)) return;
        Line line = new Line();
        marker.setNode(line);
        getPlotChildren().add(line);
        horizontalMarkers.add(marker);
    }

    public void removeHorizontalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (marker.getNode() != null) {
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        horizontalMarkers.remove(marker);
    }

    public void addVerticalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (verticalMarkers.contains(marker)) return;
        Line line = new Line();
        line.setStyle("-fx-stroke: black; -fx-opacity: 0.4");
        line.setMouseTransparent(true);
        marker.setNode(line);
        getPlotChildren().add(line);
        verticalMarkers.add(marker);
    }
    public void addVerticalZoomMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (verticalZoomMarkers.contains(marker)) return;
        Line line = new Line();
        line.setStyle("-fx-stroke: green; -fx-opacity: 0.4");
        line.setMouseTransparent(true);
        marker.setNode(line);
        getPlotChildren().add(line);
        verticalZoomMarkers.add(marker);
    }

    public void addRectangleMarker(Data<X, X> marker){
        Objects.requireNonNull(marker, "the marker must not be null");
        if (rectangleMarkers.contains(marker)) return;
        Rectangle rectangle = new Rectangle(0,0,0,0);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.2));
        rectangle.setMouseTransparent(true);
        marker.setNode(rectangle);
        getPlotChildren().add(rectangle);
        rectangleMarkers.add(marker);
    }

    public void addInfoBox(Data<X, Y> marker){
        Objects.requireNonNull(marker, "The marker must not be null");
        if(infoBoxes.contains(marker)) return;
        Rectangle infoBox = new Rectangle(10,10,10,10);
        infoBox.setStroke(Color.RED);
        infoBox.setFill(Color.BLUE.deriveColor(1,1,1,0.2));
        infoBox.setMouseTransparent(true);
        marker.setNode(infoBox);
        getPlotChildren().add(infoBox);
        infoBoxes.add(marker);
    }

    public void addStackPane(Data<X, Y> marker, String infoText, boolean isPositive){
        //TODO: ADD A CHECK TO SEE IF THERE ARE OTHER MARKER WITHIN 20PX OF THIS ONE AND THEN MOVE IT SO THAT IT DOESNT
        // LEAVE THE SCREEN BUT ALSO DOESN'T OVERLAP WITH ANOTHER STACKPANE

        Objects.requireNonNull(marker, "The marker must not be null");
        if(stackPanes.contains(marker)) return;
        StackPane stackPane = new StackPane();
        Rectangle rectangle = new Rectangle(0,0,0,0);
        if(isPositive){
            rectangle.setFill(Color.rgb(153, 255, 175).deriveColor(1, 1, 1, 0.75));
            rectangle.setStroke(Color.GREEN);

        }else{
            rectangle.setFill(Color.rgb(232, 92, 102).deriveColor(1, 1, 1, 0.75));
            rectangle.setStroke(Color.RED);
        }
        stackPane.getChildren().add(rectangle);
        Text text = new Text(infoText);
        stackPane.getChildren().add(text);
        stackPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        stackPane.setMouseTransparent(true);
        marker.setNode(stackPane);
        getPlotChildren().add(stackPane);
        stackPanes.add(marker);
    }

    public void removeVerticalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (marker.getNode() != null) {
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        verticalMarkers.remove(marker);
    }

    public void removeAllVerticalValueMarkers(){
        for(Data<X, Y> marker: verticalMarkers){
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        verticalMarkers.clear();
    }

    public void removeAllVeritcalZoomMarkers(){
        for(Data<X, Y> marker: verticalZoomMarkers){
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        verticalZoomMarkers.clear();
    }

    public void removeAllRectangleMarkers(){
        for(Data<X, X> marker : rectangleMarkers){
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        rectangleMarkers.clear();
    }

    public void removeAllSeriesLabels(){
        for(Data<X, Y> marker : infoBoxes){
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        infoBoxes.clear();
    }

    public void removeAllStackPanes(){
        for(Data<X, Y> marker: stackPanes){
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        stackPanes.clear();
    }




    //Returns only the LineCharts XYCHART.SERIES objects and not MARKERS
    public ObservableList<XYChart.Series> getSeries(){
        return super.getData();
    }

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        for (Data<X, Y> horizontalMarker : horizontalMarkers) {
            Line line = (Line) horizontalMarker.getNode();
            line.setStartX(0);
            line.setEndX(getBoundsInLocal().getWidth());
            line.setStartY(getYAxis().getDisplayPosition(horizontalMarker.getYValue()) + 0.5); // 0.5 for crispness
            line.setEndY(line.getStartY());
            line.toFront();
        }
        for (Data<X, Y> verticalMarker : verticalMarkers) {
            Line line = (Line) verticalMarker.getNode();
            line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getXValue()) + 0.5);  // 0.5 for crispness
            line.setEndX(line.getStartX());
            line.setStartY(0d);
            line.setEndY(getBoundsInLocal().getHeight());
            line.toFront();
        }
        for (Data<X, Y> verticalMarker : verticalZoomMarkers) {
            Line line = (Line) verticalMarker.getNode();
            line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getXValue()) + 0.5);  // 0.5 for crispness
            line.setEndX(line.getStartX());
            line.setStartY(0d);
            line.setEndY(getBoundsInLocal().getHeight());
            line.toFront();
        }

        for (Data<X, X> rectangleMarker : rectangleMarkers) {
            Rectangle rectangle = (Rectangle) rectangleMarker.getNode();
            rectangle.setX( getXAxis().getDisplayPosition(rectangleMarker.getXValue()) + 0.5);  // 0.5 for crispness
            rectangle.setWidth( getXAxis().getDisplayPosition(rectangleMarker.getYValue()) - getXAxis().getDisplayPosition(rectangleMarker.getXValue()));
            rectangle.setY(0d);
            rectangle.setHeight(getBoundsInLocal().getHeight());
            rectangle.toBack();
        }

        for(Data<X, Y> labelMarker : infoBoxes){
            Rectangle rectangle = (Rectangle) labelMarker.getNode();
            rectangle.setX(getXAxis().getDisplayPosition(labelMarker.getXValue()) + 0.5);
            rectangle.setWidth(50);
            rectangle.setY(getYAxis().getDisplayPosition(labelMarker.getYValue()) - 25);
            rectangle.setHeight(50);
            rectangle.toBack();
        }

        for(Data<X, Y> stackPaneMarker : stackPanes){
            StackPane stackPane = (StackPane) stackPaneMarker.getNode();
            Text theText = (Text) stackPane.getChildren().get(1);
            theText.setFont(theText.getFont());
            double width = theText.getBoundsInLocal().getWidth() + 8;
            double height = theText.getBoundsInLocal().getHeight();

            stackPane.setLayoutX(getXAxis().getDisplayPosition(stackPaneMarker.getXValue()) + (width/2) + 0.5);
            stackPane.setLayoutY(getYAxis().getDisplayPosition(stackPaneMarker.getYValue()));
            stackPane.setMinWidth(50);
            stackPane.setMinHeight(20);

            Rectangle rectangle = (Rectangle) stackPane.getChildren().get(0);

            rectangle.setMouseTransparent(true);
            rectangle.setWidth(width);
            rectangle.setHeight(height);
            stackPane.toFront();
        }
    }

    /*
    *
    *   THE FOLLOWING METHODS ARE USED BY THE CONTROLLER TO ADD AND REMOVE ITEMS TO THE LINECHART
    *
    * */


    public void hideHiddenSeries(){
        this.getSeries().get(0).getNode().setVisible(false);
        Set<Node> legends = this.lookupAll("Label.chart-legend-item");
        legends.stream().findFirst().get().setVisible(!legends.stream().findFirst().get().isVisible());
    }

    //Clears everything from the linechart
    public void fullClear(){
        this.removeAllStackPanes();
        this.removeAllVeritcalZoomMarkers();
        this.removeAllRectangleMarkers();
        this.removeAllSeriesLabels();
        this.removeAllVerticalValueMarkers();
        this.getData().clear();
    }

    public ArrayList<Data<Number, Number>> getMarkersForXValue(int index){
        ArrayList<XYChart.Data<Number,Number>> arrayList = new ArrayList<>();
        if(!this.getData().isEmpty()) {
            for (XYChart.Series series : this.getSeries()) {
                if (!(series.getName() == "HiddenSeries")) {
                    if (index < series.getData().size()) {
                        for (Object dataItem : series.getData()) {
                            var item = (XYChart.Data<Number, Number>) dataItem;
                            if (item.getXValue().intValue() == index) {
                                XYChart.Data<Number, Number> xyData = new XYChart.Data<>();
                                xyData.setYValue(item.getYValue());
                                xyData.setXValue(index);
                                xyData.setExtraValue(series.getName());
                                arrayList.add(xyData);
                                //TODO : Create a label inside of the rectangle that displays the current price
                            }
                        }
                    }
                }
            }
        }
        return arrayList;
    }


    //A method that creates all PearssonsCorrelation valeus for all combinations of series in the linechart
    public void createPearssonsCorrelations(){
        ObservableList<XYChart.Series> list = this.getSeries();
        //Exclude the hiddenSeries
        list.remove(0);
        //iterate over the rest
        for(XYChart.Series item : list){
            for(XYChart.Series item2 : list) {
                if (!(item.getName() == item2.getName())) {
                    PearsonCorrelation corr = new PearsonCorrelation();
                    double pcorrelation = corr.calculateCorrelation(item,item2);
                }
            }
        }
        //iterate over everything again and exclude correlations of the same series

    }


}