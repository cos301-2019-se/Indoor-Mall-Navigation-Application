import React from 'react';
import renderer from 'react-test-renderer';

import Scan from '../app/components/Scan';


describe('<Scan />', () => {
	it('renders correctly', () => {
		const tree = renderer.create(<Scan />).toJSON();
		expect(tree).toMatchSnapshot();
	});
	

});
