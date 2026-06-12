"use client"

import { motion } from "framer-motion"

export function ZelligePattern() {
  return (
    <div className="fixed inset-0 pointer-events-none overflow-hidden">
      {/* Animated geometric pattern overlay */}
      <svg
        className="absolute inset-0 w-full h-full opacity-[0.03]"
        xmlns="http://www.w3.org/2000/svg"
      >
        <defs>
          <pattern
            id="zellige"
            x="0"
            y="0"
            width="60"
            height="60"
            patternUnits="userSpaceOnUse"
          >
            {/* 8-pointed star - classic Moroccan zellige motif */}
            <motion.path
              d="M30 0 L35 25 L60 30 L35 35 L30 60 L25 35 L0 30 L25 25 Z"
              fill="none"
              stroke="currentColor"
              strokeWidth="0.5"
              initial={{ opacity: 0.3 }}
              animate={{ opacity: [0.3, 0.6, 0.3] }}
              transition={{ duration: 4, repeat: Infinity }}
            />
            {/* Inner diamond */}
            <motion.path
              d="M30 15 L40 30 L30 45 L20 30 Z"
              fill="none"
              stroke="currentColor"
              strokeWidth="0.3"
              initial={{ opacity: 0.2 }}
              animate={{ opacity: [0.2, 0.5, 0.2] }}
              transition={{ duration: 3, repeat: Infinity, delay: 0.5 }}
            />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#zellige)" />
      </svg>

      {/* Floating decorative elements */}
      <motion.div
        className="absolute top-20 right-10 w-32 h-32 rounded-full bg-primary/5 blur-3xl"
        animate={{
          scale: [1, 1.2, 1],
          opacity: [0.3, 0.5, 0.3],
        }}
        transition={{ duration: 8, repeat: Infinity }}
      />
      <motion.div
        className="absolute bottom-40 left-5 w-40 h-40 rounded-full bg-accent/5 blur-3xl"
        animate={{
          scale: [1.2, 1, 1.2],
          opacity: [0.4, 0.2, 0.4],
        }}
        transition={{ duration: 10, repeat: Infinity }}
      />
      <motion.div
        className="absolute top-1/2 right-0 w-24 h-24 rounded-full bg-secondary/5 blur-2xl"
        animate={{
          y: [-20, 20, -20],
          opacity: [0.3, 0.5, 0.3],
        }}
        transition={{ duration: 6, repeat: Infinity }}
      />
    </div>
  )
}
