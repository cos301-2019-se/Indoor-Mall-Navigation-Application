'use strict';
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Image} from 'react-native';
import {ScrollView, TouchableOpacity, TextInput } from 'react-native';
import SearchInput, { createFilter } from 'react-native-search-filter';
import shops from './shops';

const KEYS_TO_FILTERS = ['shop.shopName', 'categories'];

export default class Categories extends React.Component{
  constructor(props) {
     super(props);
     this.state = {
       searchTerm: ''
     }
   }
   searchUpdated(term) {
     this.setState({ searchTerm: term })
   }
   render() {
     const filteredShops = shops.filter(createFilter(this.state.searchTerm, KEYS_TO_FILTERS))
     const { navigate } = this.props.navigation;
     return (
       <View style={styles.container}>
         
         <TextInput  
          placeholder="My Location"  
          underlineColorAndroid='transparent'  
          editable={this.state.TextInputDisableHolder}  
          />
         <SearchInput 
           onChangeText={(term) => { this.searchUpdated(term) }} 
           style={styles.searchInput}
           placeholder="search shop..."
           />
         <ScrollView>
           {filteredShops.map(store => {
             return (
               <TouchableOpacity onPress={()=>alert(store.shop.shopName)} key={store.id} style={styles.shopItem}>
                 <View>
                   <Text>{store.shop.shopName}</Text>
                   <Text style={styles.categoryItem}>{store.categories}</Text>
                 </View>
               </TouchableOpacity>
             )
           })}
         </ScrollView>
       </View>
     );
   }
 }
  
 const styles = StyleSheet.create({
   container: {
     flex: 1,
     backgroundColor: '#fff',
     justifyContent: 'flex-start'
   },
   shopItem:{
     borderBottomWidth: 0.5,
     borderColor: 'rgba(0,0,0,0.3)',
     padding: 10
   },
   categoryItem: {
     color: 'rgba(0,0,0,0.5)'
   },
   searchInput:{
     padding: 10,
     borderColor: '#CCC',
     borderWidth: 1
   },
    searchIcon: {
        padding: 10,
    }
 })