import React, { useState } from 'react'
import { motion } from 'framer-motion'

export default function Button({
  children,
  onClick,
  type = 'button',
  variant = 'primary',
  loading = false,
  disabled = false,
  fullWidth = false,
  size = 'md',
  icon,
  style: extraStyle,
}) {
  const [hovered, setHovered] = useState(false)

  const isDisabled = disabled || loading

  const sizeMap = {
    sm: { padding: '10px 20px', fontSize: 14 },
    md: { padding: '14px 28px', fontSize: 15 },
    lg: { padding: '17px 36px', fontSize: 16 },
  }

  const variantStyles = {
    primary: {
      background: hovered && !isDisabled
        ? 'linear-gradient(135deg, #2563eb, #0891b2)'
        : 'linear-gradient(135deg, #3B82F6, #06b6d4)',
      color: '#fff',
      border: 'none',
      boxShadow: hovered && !isDisabled
        ? '0 8px 32px rgba(59,130,246,0.5)'
        : '0 4px 20px rgba(59,130,246,0.3)',
    },
    secondary: {
      background: hovered && !isDisabled
        ? 'rgba(59,130,246,0.15)'
        : 'rgba(59,130,246,0.08)',
      color: 'var(--color-primary)',
      border: '1px solid var(--color-border-bright)',
      boxShadow: 'none',
    },
    ghost: {
      background: 'transparent',
      color: 'var(--color-text-muted)',
      border: '1px solid var(--color-border)',
      boxShadow: 'none',
    },
  }

  return (
    <motion.button
      type={type}
      onClick={onClick}
      disabled={isDisabled}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
      whileTap={!isDisabled ? { scale: 0.97 } : {}}
      style={{
        ...sizeMap[size],
        ...variantStyles[variant],
        borderRadius: 12,
        fontFamily: 'var(--font-display)',
        fontWeight: 600,
        cursor: isDisabled ? 'not-allowed' : 'pointer',
        opacity: isDisabled ? 0.55 : 1,
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        width: fullWidth ? '100%' : 'auto',
        transition: 'all 0.25s ease',
        letterSpacing: '-0.2px',
        ...extraStyle,
      }}
    >
      {loading ? <Spinner /> : icon}
      {children}
    </motion.button>
  )
}

function Spinner() {
  return (
    <svg
      width="18" height="18" viewBox="0 0 24 24"
      fill="none" stroke="currentColor" strokeWidth="2.5"
      style={{ animation: 'spin 0.7s linear infinite' }}
    >
      <style>{`@keyframes spin { to { transform: rotate(360deg); } }`}</style>
      <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83" />
    </svg>
  )
}
