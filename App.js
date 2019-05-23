/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import { StyleSheet, Text, View} from 'react-native';
import { Button } from 'react-native-elements';
//import { Button} from 'react-native-material-design';

export default class App extends Component<Props> {
  render() {
    const { navigate } = this.props.navigation;
    return (
      <View style={styles.container}>
      <Text>This is a default entry Page</Text>
      <Text>Use the navigation buttons to go to the page you want to work on</Text>
      <Button title="Login"></Button>
      <Button title="Register"></Button>
      <Button title="Cart"></Button>
      <Button title="Search"></Button>
      <Button title="Scan" ></Button>
    </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#ffffff',
    alignItems: 'center',
    justifyContent: 'center'
  },
  btn: {
    marginTop: 8,
    padding: 8,
    backgroundColor:"red"
  },
});
