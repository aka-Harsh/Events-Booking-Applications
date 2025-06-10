import React from 'react';

const GlassCard = ({ children, className = "", hover = true }) => (
  <div className={`
    backdrop-blur-lg bg-white/10 border border-white/20 rounded-3xl shadow-2xl
    ${hover ? 'hover:bg-white/20 hover:border-white/30 hover:shadow-3xl hover:scale-105' : ''}
    ${hover ? 'transition-all duration-500 transform' : ''}
    w-full max-w-full
    ${className}
  `}>
    {children}
  </div>
);

export default GlassCard;