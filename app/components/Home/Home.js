
import React from 'react';
import { StyleSheet, Text, View} from 'react-native';
import { Button } from 'react-native-elements';
//import { Button} from 'react-native-material-design';

export default class Home extends React.Component{
  render() {
    const { navigate } = this.props.navigation;
    return (
      <View>
      <Text>This is a default entry Page</Text>
      <Text>Use the navigation buttons to go to the page you want to work on</Text>
      <Button title="Login"></Button>
      <Button title="Register"></Button>
      <Button title="Cart"></Button>
      <Button title="Search" onPress={() => this.props.navigation.navigate('Search')}></Button>
      <Button title="Scan" onPress={() => this.props.navigation.navigate('Scan')}></Button>
    </View>
    );
  }
}