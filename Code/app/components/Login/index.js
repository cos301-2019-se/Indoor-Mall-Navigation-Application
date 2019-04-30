
import React, { Component } from 'react'
import Styles from './styles';
import { StyleSheet, Text, View,TextInput, Button, Image, TouchableOpacity, Picker, ScrollView, AsyncStorage } from 'react-native';

class Login extends Component {

	constructor(props){
			super(props);
			this.state = {
				email:'',
				password:'',
            }
            /*this.validate = this.validate.bind(this);
			this.handleSubmit = this.handleSubmit.bind(this);
			this._storeData = this._storeData.bind(this);
			this.handleStart = this.handleStart.bind(this);*/
        }
    	render() {
            return (   
            
            <View>
                <Text >Login Page</Text>
                <TextInput
                        onChangeText={(text)=> this.setState({email: text}) }
						underlineColorAndroid = "#ccc"
						placeholder = "email"
                     />
                     <TextInput
                        onChangeText={(text)=> this.setState({password: text}) }
						underlineColorAndroid = "#ccc"
						placeholder = "****"
                     />
            <TouchableOpacity>
                <Text>LOGIN</Text>
            </TouchableOpacity>
            </View>
            )
        }
    }

    export default Login