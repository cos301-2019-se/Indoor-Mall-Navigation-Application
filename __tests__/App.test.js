import React from 'react';
import {shallow} from 'enzyme';
import Enzyme from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

Enzyme.configure({ adapter: new Adapter() });

import App from '../App';


describe('<App />', () => {
	it('App renders without crashing', () => {
		const rendered = shallow(<App />);
		expect(rendered).toBeTruthy();
	});
	it('renders correctly', () => {
		const tree = shallow(<App />)
		expect(tree).toMatchSnapshot();
	});

});
