import { createStackNavigator, createAppContainer } from 'react-navigation';
import Home from '../app/components/Home/Home';
import Scan from '../app/components/Scan/Scan';
import Search from '../app/components/Search/Search';
import Categories from '../app/components/Search/Categories';

const RouterStack = createStackNavigator({
  Home: { screen: Home },
  Scan: { screen: Scan },
  Search: {screen: Search},
  Categories: {screen: Categories},
});

const Router = createAppContainer(RouterStack)

export default Router;
