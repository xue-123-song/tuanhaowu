import { render, screen } from '@testing-library/react';
import App from './App';

test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByPlaceholderText('请输入内容');
  expect(linkElement).toBeInTheDocument();
});
