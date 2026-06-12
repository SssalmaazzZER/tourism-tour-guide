"use client"

import { motion } from "framer-motion"
import { Quote, ChevronLeft, ChevronRight, Star } from "lucide-react"
import { useState } from "react"

const experiences = [
  {
    id: 1,
    quote: "The colors of Morocco stay with you forever. From the blue streets of Chefchaouen to the golden dunes of the Sahara, every moment was magical.",
    author: "Sarah M.",
    location: "United States",
    rating: 5,
    trip: "10-day Adventure",
  },
  {
    id: 2,
    quote: "The hospitality of Moroccan people made our journey unforgettable. The riads, the food, the music - everything exceeded our expectations!",
    author: "Pierre L.",
    location: "France",
    rating: 5,
    trip: "2-week Cultural Tour",
  },
  {
    id: 3,
    quote: "Watching the sunset over the Atlas Mountains while sipping mint tea... that&apos;s a memory I&apos;ll cherish forever.",
    author: "Emma K.",
    location: "Australia",
    rating: 5,
    trip: "Mountain Retreat",
  },
]

export function TestimonialsSection() {
  const [currentIndex, setCurrentIndex] = useState(0)

  const nextTestimonial = () => {
    setCurrentIndex((prev) => (prev + 1) % experiences.length)
  }

  const prevTestimonial = () => {
    setCurrentIndex((prev) => (prev - 1 + experiences.length) % experiences.length)
  }

  return (
    <section className="px-4 py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center justify-between mb-4"
      >
        <div>
          <h2 className="text-lg font-semibold">Traveler Stories</h2>
          <p className="text-xs text-muted-foreground">Real experiences from visitors</p>
        </div>
        <div className="flex gap-2">
          <motion.button
            onClick={prevTestimonial}
            className="w-8 h-8 rounded-lg bg-muted/50 border border-border/50 flex items-center justify-center"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
          >
            <ChevronLeft className="w-4 h-4" />
          </motion.button>
          <motion.button
            onClick={nextTestimonial}
            className="w-8 h-8 rounded-lg bg-muted/50 border border-border/50 flex items-center justify-center"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
          >
            <ChevronRight className="w-4 h-4" />
          </motion.button>
        </div>
      </motion.div>

      <motion.div
        key={currentIndex}
        initial={{ opacity: 0, x: 50 }}
        animate={{ opacity: 1, x: 0 }}
        exit={{ opacity: 0, x: -50 }}
        transition={{ duration: 0.3 }}
        className="relative rounded-3xl bg-gradient-to-br from-muted/50 to-muted/20 border border-border/50 p-6"
      >
        {/* Quote icon */}
        <motion.div
          className="absolute -top-3 left-6 w-10 h-10 rounded-xl bg-primary flex items-center justify-center"
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.2, type: "spring" }}
        >
          <Quote className="w-5 h-5 text-background" />
        </motion.div>

        {/* Content */}
        <div className="pt-4">
          <motion.p 
            className="text-sm leading-relaxed mb-4 italic"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.3 }}
          >
            {experiences[currentIndex].quote}
          </motion.p>

          <div className="flex items-center justify-between">
            <div>
              <p className="font-semibold">{experiences[currentIndex].author}</p>
              <p className="text-xs text-muted-foreground">
                {experiences[currentIndex].location} • {experiences[currentIndex].trip}
              </p>
            </div>
            <div className="flex gap-0.5">
              {[...Array(experiences[currentIndex].rating)].map((_, i) => (
                <Star 
                  key={i} 
                  className="w-4 h-4 fill-secondary text-secondary" 
                />
              ))}
            </div>
          </div>
        </div>

        {/* Dots indicator */}
        <div className="flex justify-center gap-2 mt-4">
          {experiences.map((_, index) => (
            <motion.button
              key={index}
              className={`w-2 h-2 rounded-full transition-all ${
                index === currentIndex 
                  ? "bg-primary w-6" 
                  : "bg-muted-foreground/30"
              }`}
              onClick={() => setCurrentIndex(index)}
              whileHover={{ scale: 1.2 }}
            />
          ))}
        </div>
      </motion.div>
    </section>
  )
}
