/**
 * 
 *  File Name: Navigation.js (path: component/app/navigation.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *  Date        Author           Changes
 *  --------------------------------------------
 *  22/05/2019  Thabo Ntsoane    Original
 *  Functional Description: This program file searches and navigates user to a specific shop
 *  Error Messages: Shop does not exist
 *  Constraints: Can only be used to navigate
 *  Assumptions: It is assumed that the user will be navigated to destination appropriately
 *
 */
'use strict';
import React, {Component} from 'react';
import { Button } from 'react-native-elements';
import { AppRegistry, StyleSheet, Text, TouchableOpacity, View ,ImageBackground} from 'react-native';
import SearchableDropdown from 'react-native-searchable-dropdown';

var items = [
    {
      id: 1,
      name: 'ABSA',
    },
    {
      id: 2,
      name: 'Bidvest',
    },
    {
      id: 3,
      name: 'CellC',
    },
    {
      id: 4,
      name: 'Checkers',
    },
    {
      id: 5,
      name: 'Cotton On',
    },
    {
      id: 6,
      name: 'PEP',
    },
    {
      id: 7,
      name: 'Woolworths',
    },
    {
      id: 8,
      name: 'Jet',
    },
    {
        id: 9,
        name: 'Side Step',
      },
      {
        id: 10,
        name: 'Pick n Pay Liqour',
      },
      {
        id: 11,
        name: 'Pick n Pay Clothing',
      },
      {
        id: 8,
        name: 'Pick n Pay',
      },
      {
        id: 8,
        name: 'Zara',
      },
  ];


export default class Search extends React.Component {
    render() {
        const { navigate } = this.props.navigation;
      return (
     
      <ImageBackground source={require('./floorplan.png')}  style={styles.backgroundImage}>
          <SearchableDropdown
        /*onTextChange={text => alert(text)}*/
        onItemSelect={item => alert(JSON.stringify(item))}
        containerStyle={{ padding: 5 }}
        textInputStyle={{
          padding: 12,
          borderWidth: 1,
          borderColor: '#ccc',
          borderRadius: 5,
        }}
        itemStyle={{
          padding: 10,
          marginTop: 2,
          backgroundColor: '#ddd',
          borderColor: '#bbb',
          borderWidth: 1,
          borderRadius: 5,
        }}
        itemTextStyle={{ color: '#222' }}
        itemsContainerStyle={{ maxHeight: 140 }}
        items={items}
        defaultIndex={2}
        placeholder="Search for shop..."
        resetValue={false}
        underlineColorAndroid="transparent"
      >
      </SearchableDropdown>
      <Button title="Categories" style={styles.category} onPress={() => this.props.navigation.navigate('Categories')}></Button>
      </ImageBackground>
    );
  }
  
}
var styles = StyleSheet.create({
    container: {
         flex: 1,
         justifyContent: 'center',
         alignItems: 'center',
         backgroundColor: '#F5FCFF',
         flexDirection: 'column',
    },
         backgroundImage:{
         width:350,
         height:560,
    },
        category:{
            position: 'absolute',
            bottom:0,
            flex: 1,
            justifyContent: 'flex-end',
            marginBottom: 1,
    },
    Button:{
        position: 'absolute',
        bottom:0,
        flex: 1,
        justifyContent: 'flex-end',
        marginBottom: 1,
    }
});
