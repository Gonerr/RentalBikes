module.exports = {
  env: {
    browser: true,
    es2021: true,
    node: true
  },
  extends: [
    'eslint:recommended',
    'plugin:react/recommended'
  ],
  parserOptions: {
    ecmaFeatures: {
      jsx: true
    },
    ecmaVersion: 12,
    sourceType: 'module'
  },
  plugins: [
    'react'
  ],
  rules: {
    'no-unused-expressions': 'off',
    'no-undef': 'off',
    'no-restricted-globals': 'off',
    'no-unused-vars': 'warn',
    'react/prop-types': 'off',
    'no-cond-assign': 'off',
    'no-useless-escape': 'off',
    'no-empty': 'off'
  },
  settings: {
    react: {
      version: 'detect'
    }
  },
  ignorePatterns: [
    '**/*.min.js',
    'src/utils/gsap/**/*.js'
  ]
}; 