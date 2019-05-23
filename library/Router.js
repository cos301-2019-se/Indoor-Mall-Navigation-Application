import { createStackNavigator, createAppContainer } from 'react-navigation';
import Home from '../app/components/Home/Home';
import Scan from '../app/components/Scan/Scan';

const RouterStack = createStackNavigator({
  Home: { screen: Home },
  Scan: { screen: Scan },
});

const Router = createAppContainer(RouterStack)

export default Router;
