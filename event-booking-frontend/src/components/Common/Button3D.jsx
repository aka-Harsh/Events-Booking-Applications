import React from 'react';

const Button3D = ({ children, onClick, variant = "primary", className = "", disabled = false, ...props }) => {
  const variants = {
    primary: "from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 text-white",
    secondary: "from-gray-400 to-gray-600 hover:from-gray-500 hover:to-gray-700 text-white",
    success: "from-green-500 to-emerald-600 hover:from-green-600 hover:to-emerald-700 text-white",
    danger: "from-red-500 to-red-600 hover:from-red-600 hover:to-red-700 text-white",
    warning: "from-yellow-500 to-orange-600 hover:from-yellow-600 hover:to-orange-700 text-white"
  };

  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`
        bg-gradient-to-r ${variants[variant]}
        px-4 py-2 sm:px-6 sm:py-3 rounded-2xl font-bold shadow-lg
        transform transition-all duration-300 
        hover:scale-105 hover:shadow-2xl hover:-translate-y-1
        active:scale-95 active:translate-y-0
        disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none
        text-sm sm:text-base
        flex items-center justify-center
        whitespace-nowrap
        ${className}
      `}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button3D;