import { createStackNavigator, createAppContainer } from 'react-navigation';
import Home from '../app/components/Home/Home';

const RouterStack = createStackNavigator({
  Home: { screen: Home },
});

const Router = createAppContainer(RouterStack)

export default Router;
