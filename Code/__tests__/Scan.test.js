import React from 'react';
import renderer from 'react-test-renderer';

import Scan from '../app/components/Scan';


describe('<Scan />', () => {
	it('Scan renders without crashing', () => {
		const rendered = renderer.create(<Scan />).toJSON();
		expect(rendered).toBeTruthy();
	});
	it('renders correctly', () => {
		const tree = renderer.create(<Scan />).toJSON();
		expect(tree).toMatchSnapshot();
	});


});
