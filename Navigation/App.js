/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {
  Component
}                             from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
  Text,
  FlatList,
  ListView,
  DeviceEventEmitter
}                             from 'react-native';
import Beacons                from 'react-native-beacons-manager';


type Props = {};
export default class App extends Component<Props> {

  constructor(props) {
    super(props);
    // Create our dataSource which will be displayed in the ListView
    var ds = new ListView.DataSource({
      rowHasChanged: (r1, r2) => r1 !== r2 }
    );
    this.state = {
      // region information
      identifier: 'ibeacon-2019-05-01',
      uuid: '​4edf0c20-f1b0-4d0d-8c2e-fd0758057dfe',
      // React Native ListView datasource initialization
      dataSource: ds.cloneWithRows([])
    };
  }

  componentDidMount() {
    //
    // component state aware here - attach events
    //
    // Ranging: Listen for beacon changes
    this.beaconsDidRange = DeviceEventEmitter.addListener(
      'beaconsDidRange',
      (data) => {
        this.setState({
          dataSource: this.state.dataSource.cloneWithRows(data.beacons)
        });
      }
    );


  }

  componentWillUnMount(){
    this.beaconsDidRange = null;
  }



  render() {
    const dataSource =  this.state;
    return (
      <View style={styles.container}>
        <Text style={styles.headline}>
          All beacons in the area
        </Text>
        <FlatList
          dataSource={ dataSource }
          enableEmptySections={ true }
          renderRow={this.renderRow}
        />
      </View>
    );
  }
renderRow = rowData => {
    return (
      <View style={styles.row}>
        <Text style={styles.smallText}>
          UUID: {rowData.uuid ? rowData.uuid  : 'NA'}
        </Text>
        <Text style={styles.smallText}>
          Major: {rowData.major ? rowData.major : 'NA'}
        </Text>
        <Text style={styles.smallText}>
          Minor: {rowData.minor ? rowData.minor : 'NA'}
        </Text>
        <Text>
          RSSI: {rowData.rssi ? rowData.rssi : 'NA'}
        </Text>
        <Text>
          Proximity: {rowData.proximity ? rowData.proximity : 'NA'}
        </Text>
        <Text>
          Distance: {rowData.accuracy ? rowData.accuracy.toFixed(2) : 'NA'}m
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 60,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  btleConnectionStatus: {
    fontSize: 20,
    paddingTop: 20
  },
  headline: {
    fontSize: 20,
    paddingTop: 20
  },
  row: {
    padding: 8,
    paddingBottom: 16
  },
    smallText: {
    fontSize: 11
  }
});


AppRegistry.registerComponent(
  'RNbeaconArticle',
  () => RNbeaconArticle
);
