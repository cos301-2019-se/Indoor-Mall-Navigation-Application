import React from 'react';
import {shallow} from 'enzyme';
import Enzyme from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

Enzyme.configure({ adapter: new Adapter() });


import Scan from '../app/components/Scan';


describe('<Scan />', () => {
	it('Scan renders without crashing', () => {
		const rendered = shallow(<Scan />);
		expect(rendered).toBeTruthy();
	});
	it('renders correctly', () => {
		const tree = shallow(<Scan />);
		expect(tree).toMatchSnapshot();
	});


});
